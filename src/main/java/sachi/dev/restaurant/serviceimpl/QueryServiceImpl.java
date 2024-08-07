package sachi.dev.restaurant.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.QueryDTO;
import sachi.dev.restaurant.model.Query;
import sachi.dev.restaurant.model.QueryStatus;
import sachi.dev.restaurant.repository.QueryRepository;
import sachi.dev.restaurant.service.QueryService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QueryDTO findById(String id) {
        return modelMapper.map(queryRepository.findById(id), QueryDTO.class);
    }

    @Override
    public List<QueryDTO> findAllByCustomerId(String customerId) {
        return queryRepository.findAllByCustomerId(customerId);
    }


    @Override
    public QueryDTO save(QueryDTO queryDTO) {
        Query query = modelMapper.map(queryDTO, Query.class);
        query.setCreatedAt(new Date());
        query.setStatus(QueryStatus.PENDING);
        return modelMapper.map(queryRepository.save(query), QueryDTO.class);
    }

    @Override
    public QueryDTO updateRespond(QueryDTO queryDTO,String id) {
        QueryDTO queryDTO1=findById(id);
        queryDTO1.setRespondedBy(queryDTO.getRespondedBy());
        queryDTO1.setRespondedAt(new Date());
        queryDTO1.setRespondedMessage(queryDTO.getRespondedMessage());
        queryDTO1.setStatus(QueryStatus.RESPONDED);
        return save(queryDTO1);
    }

    @Override
    public List<QueryDTO> findAllByRestaurantId(String restaurantId) {
        return queryRepository.findAllByRestaurantId(restaurantId);
    }
}
