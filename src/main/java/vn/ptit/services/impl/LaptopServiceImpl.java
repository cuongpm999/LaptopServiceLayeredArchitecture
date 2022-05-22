package vn.ptit.services.impl;

import com.google.gson.Gson;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import vn.ptit.dtos.CommentDTO;
import vn.ptit.dtos.LaptopDTO;
import vn.ptit.dtos.ManufacturerDTO;
import vn.ptit.entities.Message;
import vn.ptit.entities.elasticsearch.EComment;
import vn.ptit.entities.elasticsearch.ELaptop;
import vn.ptit.entities.mysql.Comment;
import vn.ptit.entities.mysql.Laptop;
import vn.ptit.entities.mysql.Manufacturer;
import vn.ptit.repositories.elasticsearch.ECommentRepository;
import vn.ptit.repositories.elasticsearch.ELaptopRepository;
import vn.ptit.repositories.elasticsearch.EManufacturerRepository;
import vn.ptit.repositories.mysql.CommentRepository;
import vn.ptit.repositories.mysql.LaptopRepository;
import vn.ptit.repositories.mysql.ManufacturerRepository;
import vn.ptit.services.LaptopService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LaptopServiceImpl implements LaptopService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    LaptopRepository laptopRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ManufacturerRepository manufacturerRepository;
    @Autowired
    private ElasticsearchOperations operations;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    ELaptopRepository eLaptopRepository;
    @Autowired
    EManufacturerRepository eManufacturerRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ECommentRepository eCommentRepository;
    private RestTemplate rest = new RestTemplate();

    @Override
    @Transactional
    public LaptopDTO save(LaptopDTO laptopDTO) {
        Laptop laptop = modelMapper.map(laptopDTO, Laptop.class);
        laptop = laptopRepository.save(laptop);

        ELaptop eLaptop = modelMapper.map(laptop, ELaptop.class);
        eLaptop.setManufacturerId(String.valueOf(laptop.getManufacturer().getId()));
        eLaptopRepository.save(eLaptop);

        laptopDTO = modelMapper.map(laptop, LaptopDTO.class);

        Message message = new Message(null, "Sản phẩm mới cập nhật", laptop.getName());

        rest.postForObject("http://localhost:9696/notification/tokens", message, Message.class);

        return laptopDTO;
    }

    @Override
    public List<LaptopDTO> findAll() {
        List<Laptop> laptops = laptopRepository.findAllWithStatusTrue();
        List<LaptopDTO> laptopDTOS = new ArrayList<>();
        laptops.forEach(laptop -> {
            LaptopDTO laptopDTO = modelMapper.map(laptop, LaptopDTO.class);
            laptopDTOS.add(laptopDTO);
        });
        return laptopDTOS;
    }

    @Override
    public LaptopDTO findById(int id) {
        Optional<Laptop> laptop = laptopRepository.findById(id);
        if (laptop.isPresent()) {
            LaptopDTO laptopDTO = modelMapper.map(laptop.get(), LaptopDTO.class);
            return laptopDTO;
        }
        return null;
    }

    @Override
    public void delete(int id) {
        Laptop laptop = laptopRepository.findById(id).get();
        laptop.setStatus(false);
        laptopRepository.save(laptop);

        UpdateRequest request = new UpdateRequest("laptops", String.valueOf(id));
        Map<String, Object> map = new HashMap<>();
        map.put("status", false);
        request.doc(map);
        try {
            client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LaptopDTO> findByName(String name) {
        List<ELaptop> laptops = new ArrayList<>();
        Query query = new StringQuery("{\n" +
                "    \"bool\": {\n" +
                "        \"must\": [\n" +
                "            {\n" +
                "                \"term\": {\n" +
                "                    \"status\": {\n" +
                "                        \"value\": true\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"bool\": {\n" +
                "                    \"should\": [\n" +
                "                        {\n" +
                "                            \"match\": {\n" +
                "                                \"name\": {\n" +
                "                                    \"query\": \"" + name + "\",\n" +
                "                                    \"operator\": \"and\",\n" +
                "                                    \"fuzziness\": 1\n" +
                "                                }\n" +
                "                            }\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"wildcard\": {\n" +
                "                                \"name\": {\n" +
                "                                    \"value\": \"*" + name + "*\"\n" +
                "                                }\n" +
                "                            }\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}");
//        query.setPageable(PageRequest.of(0, 1000));
        SearchHits<ELaptop> searchHits = operations.search(query, ELaptop.class);

        for (SearchHit<ELaptop> hit : searchHits) {
            laptops.add(hit.getContent());
        }
        List<LaptopDTO> laptopDTOS = new ArrayList<>();
        laptops.forEach(laptop -> {
            LaptopDTO laptopDTO = modelMapper.map(laptop, LaptopDTO.class);
            ManufacturerDTO manufacturerDTO = modelMapper.map(eManufacturerRepository.findById(laptop.getManufacturerId()), ManufacturerDTO.class);
            laptopDTO.setManufacturer(manufacturerDTO);
            laptopDTOS.add(laptopDTO);
        });
        return laptopDTOS;
    }

    @Override
    public List<LaptopDTO> findAllWithManufacturer(String idManufacturer, Integer limit) {
        Pageable pageable =
                PageRequest.of(0, limit);
        Page<ELaptop> laptops = eLaptopRepository.findByManufacturerIdAndStatusTrueOrderByCreatedAtDesc(idManufacturer, pageable);
        List<LaptopDTO> laptopDTOS = new ArrayList<>();
        laptops.forEach(laptop -> {
            LaptopDTO laptopDTO = modelMapper.map(laptop, LaptopDTO.class);
            ManufacturerDTO manufacturerDTO = modelMapper.map(eManufacturerRepository.findById(laptop.getManufacturerId()), ManufacturerDTO.class);
            laptopDTO.setManufacturer(manufacturerDTO);

            List<Comment> comments = commentRepository.findByLaptop_IdOrderByCreatedAtDesc(Integer.parseInt(laptop.getId()));
            List<CommentDTO> commentDTOS = new ArrayList<>();
            comments.forEach(c -> {
                commentDTOS.add(modelMapper.map(c, CommentDTO.class));
            });

            laptopDTO.setComments(commentDTOS);

            Double star = laptopRepository.avgStar(Integer.parseInt(laptop.getId()));
            if (star != null)
                laptopDTO.setAverageStar(star);

            laptopDTOS.add(laptopDTO);
        });
        return laptopDTOS;
    }

    @Override
    public List<LaptopDTO> findSameItem(String idManufacturer, String idLaptop) {
        List<ELaptop> laptops = eLaptopRepository.findByManufacturerIdAndStatusTrueAndIdNot(idManufacturer, idLaptop);
        List<LaptopDTO> laptopDTOS = new ArrayList<>();
        laptops.forEach(laptop -> {
            LaptopDTO laptopDTO = modelMapper.map(laptop, LaptopDTO.class);
            ManufacturerDTO manufacturerDTO = modelMapper.map(eManufacturerRepository.findById(laptop.getManufacturerId()), ManufacturerDTO.class);
            laptopDTO.setManufacturer(manufacturerDTO);

            List<Comment> comments = commentRepository.findByLaptop_IdOrderByCreatedAtDesc(Integer.parseInt(laptop.getId()));
            List<CommentDTO> commentDTOS = new ArrayList<>();
            comments.forEach(c -> {
                commentDTOS.add(modelMapper.map(c, CommentDTO.class));
            });
            laptopDTO.setComments(commentDTOS);

            Double star = laptopRepository.avgStar(Integer.parseInt(laptop.getId()));
            if (star != null)
                laptopDTO.setAverageStar(star);

            laptopDTOS.add(laptopDTO);
        });
        return laptopDTOS;
    }

    @Override
    public List<LaptopDTO> filter(String tabName, String manufacturerId) {
        List<ELaptop> laptops = new ArrayList<>();
        String mustQuery = "{\n" +
                "          \"term\": {\n" +
                "            \"status\": {\n" +
                "              \"value\": true\n" +
                "            }\n" +
                "          }\n" +
                "        }," +
                "       {\n" +
                "          \"term\": {\n" +
                "            \"manufacturerId\": {\n" +
                "              \"value\": \"" + manufacturerId + "\"\n" +
                "            }\n" +
                "          }\n" +
                "        }";

        Query query = new StringQuery("{\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" + mustQuery +
                "      ]\n" +
                "    }\n" +
                "  }");

//        query.setPageable(PageRequest.of(0, 1000));

        SearchHits<ELaptop> searchHits = operations.search(query, ELaptop.class);

        for (SearchHit<ELaptop> hit : searchHits) {
            laptops.add(hit.getContent());
        }
        List<LaptopDTO> laptopDTOS = new ArrayList<>();
        laptops.forEach(laptop -> {
            LaptopDTO laptopDTO = modelMapper.map(laptop, LaptopDTO.class);
            ManufacturerDTO manufacturerDTO = modelMapper.map(eManufacturerRepository.findById(laptop.getManufacturerId()), ManufacturerDTO.class);
            laptopDTO.setManufacturer(manufacturerDTO);

            List<Comment> comments = commentRepository.findByLaptop_IdOrderByCreatedAtDesc(Integer.parseInt(laptop.getId()));
            List<CommentDTO> commentDTOS = new ArrayList<>();
            comments.forEach(c -> {
                commentDTOS.add(modelMapper.map(c, CommentDTO.class));
            });
            laptopDTO.setComments(commentDTOS);

            Double star = laptopRepository.avgStar(Integer.parseInt(laptop.getId()));
            if (star != null)
                laptopDTO.setAverageStar(star);

            laptopDTOS.add(laptopDTO);
        });


        if (tabName != null) {
            if (tabName.equalsIgnoreCase("new")) {
                laptopDTOS.sort(new Comparator<LaptopDTO>() {
                    @Override
                    public int compare(LaptopDTO o1, LaptopDTO o2) {
                        return Long.compare(o2.getCreatedAt().getTime(), o1.getCreatedAt().getTime());
                    }
                });
            }
            if (tabName.equalsIgnoreCase("discount")) {
                List<LaptopDTO> res = laptopDTOS.stream().filter(l -> l.getDiscount() > 0).collect(Collectors.toList());
                laptopDTOS.clear();
                laptopDTOS.addAll(res);
            }
            if (tabName.equalsIgnoreCase("low-to-high")) {
                laptopDTOS.sort(new Comparator<LaptopDTO>() {
                    @Override
                    public int compare(LaptopDTO o1, LaptopDTO o2) {
                        double price1 = o1.getPrice() * (100 - o1.getDiscount());
                        double price2 = o2.getPrice() * (100 - o2.getDiscount());
                        return Double.compare(price1, price2);
                    }
                });
            }

            if (tabName.equalsIgnoreCase("high-to-low")) {
                laptopDTOS.sort(new Comparator<LaptopDTO>() {
                    @Override
                    public int compare(LaptopDTO o1, LaptopDTO o2) {
                        double price1 = o1.getPrice() * (100 - o1.getDiscount());
                        double price2 = o2.getPrice() * (100 - o2.getDiscount());
                        return Double.compare(price2, price1);
                    }
                });
            }
        }
        return laptopDTOS;
    }

    @Override
    public List<LaptopDTO> filter(String tabName, Integer manufacturerId, String cpu, String ram,
                                  String hardDrive, String vga) {
        String jpql = "select p from Laptop p where p.status = true";
        if (manufacturerId != null && manufacturerId != -1) {
            jpql += " and p.manufacturer.id =" + manufacturerId;
        }
        if (cpu != null) {
            String datas[] = cpu.split("\\,");
            for (int i = 0; i < datas.length; i++) {
                if (i == 0)
                    jpql += " and (p.cpu like '%" + datas[i] + "%'";
                else jpql += " or p.cpu like '%" + datas[i] + "%'";
                if (i == datas.length - 1) jpql += ")";
            }
        }
        if (ram != null) {
            String datas[] = ram.split("\\,");
            for (int i = 0; i < datas.length; i++) {
                if (i == 0)
                    jpql += " and (p.ram like '%" + datas[i] + "%'";
                else jpql += " or p.ram like '%" + datas[i] + "%'";
                if (i == datas.length - 1) jpql += ")";
            }
        }
        if (hardDrive != null) {
            String datas[] = hardDrive.split("\\,");
            for (int i = 0; i < datas.length; i++) {
                if (i == 0)
                    jpql += " and (p.hardDrive like '%" + datas[i] + "%'";
                else jpql += " or p.hardDrive like '%" + datas[i] + "%'";
                if (i == datas.length - 1) jpql += ")";
            }
        }
        if (vga != null) {
            String datas[] = vga.split("\\,");
            for (int i = 0; i < datas.length; i++) {
                if (i == 0)
                    jpql += " and (p.vga like '%" + datas[i] + "%'";
                else jpql += " or p.vga like '%" + datas[i] + "%'";
                if (i == datas.length - 1) jpql += ")";
            }
        }
        javax.persistence.Query query = entityManager.createQuery(jpql, Laptop.class);
        List<Laptop> laptops = query.getResultList();

        List<LaptopDTO> laptopDTOS = new ArrayList<>();

        laptops.forEach(laptop -> {
            LaptopDTO laptopDTO = modelMapper.map(laptop, LaptopDTO.class);

            List<Comment> comments = commentRepository.findByLaptop_IdOrderByCreatedAtDesc(laptop.getId());
            List<CommentDTO> commentDTOS = new ArrayList<>();
            comments.forEach(c -> {
                commentDTOS.add(modelMapper.map(c, CommentDTO.class));
            });
            laptopDTO.setComments(commentDTOS);

            Double star = laptopRepository.avgStar(laptop.getId());
            if (star != null)
                laptopDTO.setAverageStar(star);

            laptopDTOS.add(laptopDTO);
        });
//        laptopDTOS = laptops.stream().map(l -> modelMapper.map(l, LaptopDTO.class)).collect(Collectors.toList());

        if (tabName != null) {
            if (tabName.equalsIgnoreCase("new")) {
                laptopDTOS.sort(new Comparator<LaptopDTO>() {
                    @Override
                    public int compare(LaptopDTO o1, LaptopDTO o2) {
                        return Long.compare(o2.getCreatedAt().getTime(), o1.getCreatedAt().getTime());
                    }
                });
            }
            if (tabName.equalsIgnoreCase("discount")) {
                List<LaptopDTO> res = laptopDTOS.stream().filter(l -> l.getDiscount() > 0).collect(Collectors.toList());
                laptopDTOS.clear();
                laptopDTOS.addAll(res);
            }
            if (tabName.equalsIgnoreCase("low-to-high")) {
                laptopDTOS.sort(new Comparator<LaptopDTO>() {
                    @Override
                    public int compare(LaptopDTO o1, LaptopDTO o2) {
                        double price1 = o1.getPrice() * (100 - o1.getDiscount());
                        double price2 = o2.getPrice() * (100 - o2.getDiscount());
                        return Double.compare(price1, price2);
                    }
                });
            }

            if (tabName.equalsIgnoreCase("high-to-low")) {
                laptopDTOS.sort(new Comparator<LaptopDTO>() {
                    @Override
                    public int compare(LaptopDTO o1, LaptopDTO o2) {
                        double price1 = o1.getPrice() * (100 - o1.getDiscount());
                        double price2 = o2.getPrice() * (100 - o2.getDiscount());
                        return Double.compare(price2, price1);
                    }
                });
            }
        }

        return laptopDTOS;
    }

    @Override
    public List<LaptopDTO> filterWithName(String tabName, String name) {
        List<ELaptop> laptops = new ArrayList<>();
        Query query = new StringQuery("{\n" +
                "    \"bool\": {\n" +
                "        \"must\": [\n" +
                "            {\n" +
                "                \"term\": {\n" +
                "                    \"status\": {\n" +
                "                        \"value\": true\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"bool\": {\n" +
                "                    \"should\": [\n" +
                "                        {\n" +
                "                            \"match\": {\n" +
                "                                \"name\": {\n" +
                "                                    \"query\": \"" + name + "\",\n" +
                "                                    \"operator\": \"and\",\n" +
                "                                    \"fuzziness\": 1\n" +
                "                                }\n" +
                "                            }\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"wildcard\": {\n" +
                "                                \"name\": {\n" +
                "                                    \"value\": \"*" + name + "*\"\n" +
                "                                }\n" +
                "                            }\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}");
//        query.setPageable(PageRequest.of(0, 1000));
        SearchHits<ELaptop> searchHits = operations.search(query, ELaptop.class);

        for (SearchHit<ELaptop> hit : searchHits) {
            laptops.add(hit.getContent());
        }
        List<LaptopDTO> laptopDTOS = new ArrayList<>();
        laptops.forEach(laptop -> {
            LaptopDTO laptopDTO = modelMapper.map(laptop, LaptopDTO.class);
            ManufacturerDTO manufacturerDTO = modelMapper.map(eManufacturerRepository.findById(laptop.getManufacturerId()), ManufacturerDTO.class);
            laptopDTO.setManufacturer(manufacturerDTO);

            List<Comment> comments = commentRepository.findByLaptop_IdOrderByCreatedAtDesc(Integer.parseInt(laptop.getId()));
            List<CommentDTO> commentDTOS = new ArrayList<>();
            comments.forEach(c -> {
                commentDTOS.add(modelMapper.map(c, CommentDTO.class));
            });
            laptopDTO.setComments(commentDTOS);

            Double star = laptopRepository.avgStar(Integer.parseInt(laptop.getId()));
            if (star != null)
                laptopDTO.setAverageStar(star);

            laptopDTOS.add(laptopDTO);
        });

        if (tabName != null) {
            if (tabName.equalsIgnoreCase("new")) {
                laptopDTOS.sort(new Comparator<LaptopDTO>() {
                    @Override
                    public int compare(LaptopDTO o1, LaptopDTO o2) {
                        return Long.compare(o2.getCreatedAt().getTime(), o1.getCreatedAt().getTime());
                    }
                });
            }
            if (tabName.equalsIgnoreCase("discount")) {
                List<LaptopDTO> res = laptopDTOS.stream().filter(l -> l.getDiscount() > 0).collect(Collectors.toList());
                laptopDTOS.clear();
                laptopDTOS.addAll(res);
            }
            if (tabName.equalsIgnoreCase("low-to-high")) {
                laptopDTOS.sort(new Comparator<LaptopDTO>() {
                    @Override
                    public int compare(LaptopDTO o1, LaptopDTO o2) {
                        double price1 = o1.getPrice() * (100 - o1.getDiscount());
                        double price2 = o2.getPrice() * (100 - o2.getDiscount());
                        return Double.compare(price1, price2);
                    }
                });
            }

            if (tabName.equalsIgnoreCase("high-to-low")) {
                laptopDTOS.sort(new Comparator<LaptopDTO>() {
                    @Override
                    public int compare(LaptopDTO o1, LaptopDTO o2) {
                        double price1 = o1.getPrice() * (100 - o1.getDiscount());
                        double price2 = o2.getPrice() * (100 - o2.getDiscount());
                        return Double.compare(price2, price1);
                    }
                });
            }
        }

        return laptopDTOS;
    }

    @Override
    @Transactional
    public LaptopDTO insertComment(CommentDTO commentDTO) {
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        Laptop laptop = new Laptop();
        laptop.setId(commentDTO.getLaptopId());
        comment.setLaptop(laptop);

        comment = commentRepository.save(comment);

        EComment eComment = modelMapper.map(comment, EComment.class);
        eComment.setLaptopId(String.valueOf(comment.getLaptop().getId()));
        eComment.setUserId(String.valueOf(comment.getUser().getId()));
        eCommentRepository.save(eComment);

        LaptopDTO laptopDTO = new LaptopDTO();
        List<Comment> comments = commentRepository.findByLaptop_IdOrderByCreatedAtDesc(commentDTO.getLaptopId());
        List<CommentDTO> commentDTOS = new ArrayList<>();
        comments.forEach(c -> {
            commentDTOS.add(modelMapper.map(c, CommentDTO.class));
        });
        double star = laptopRepository.avgStar(commentDTO.getLaptopId());
        laptopDTO.setComments(commentDTOS);
        laptopDTO.setAverageStar(star);

        return laptopDTO;
    }

}
