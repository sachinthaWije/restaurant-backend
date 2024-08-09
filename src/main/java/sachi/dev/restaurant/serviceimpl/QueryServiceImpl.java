package sachi.dev.restaurant.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.QueryDTO;
import sachi.dev.restaurant.model.Query;
import sachi.dev.restaurant.model.QueryStatus;
import sachi.dev.restaurant.model.Restaurant;
import sachi.dev.restaurant.model.User;
import sachi.dev.restaurant.repository.QueryRepository;
import sachi.dev.restaurant.repository.RestaurantRepository;
import sachi.dev.restaurant.repository.UserRepository;
import sachi.dev.restaurant.service.QueryService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public QueryDTO findById(String id) {
        return modelMapper.map(queryRepository.findById(id), QueryDTO.class);
    }

    @Override
    public List<QueryDTO> findAllByCustomerId(String customerId) {
       List<QueryDTO> list= queryRepository.findAllByCustomerId(customerId);
       for(QueryDTO queryDTO:list){
           Optional<Restaurant> optionalRestaurantDTO = restaurantRepository.findById(queryDTO.getRestaurantId());
           String restaurantName = optionalRestaurantDTO.get().getName() + " - " + optionalRestaurantDTO.get().getLocation();

           queryDTO.setRestaurantName(restaurantName);
       }
        return list;
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
        List<QueryDTO> list= queryRepository.findAllByRestaurantId(restaurantId);
        for(QueryDTO queryDTO:list){
           Optional<User> user=  userRepository.findById(queryDTO.getCustomerId());
           queryDTO.setCustomerName(user.get().getUsername());

           if(queryDTO.getRespondedBy()!=null){
               Optional<User> staff=  userRepository.findById(queryDTO.getRespondedBy());
               queryDTO.setRespondedBy(staff.get().getUsername());
           }
        }
        return list;
    }
}
