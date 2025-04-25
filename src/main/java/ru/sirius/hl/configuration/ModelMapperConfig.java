package ru.sirius.hl.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.sirius.hl.dto.TicketDto;
import ru.sirius.hl.model.Ticket;

@Configuration
public class ModelMapperConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        mapper.typeMap(Ticket.class, TicketDto.class)
                .addMapping(src -> src.getMovie().getId(), TicketDto::setMovie)
                .addMapping(src -> src.getCustomer().getId(), TicketDto::setCustomer);
        mapper.typeMap(TicketDto.class, Ticket.class)
                .addMapping(TicketDto::getMovie, (dest, value) -> dest.getMovie().setId((Long) value))
                .addMapping(TicketDto::getCustomer, (dest, value) -> dest.getCustomer().setId((Long) value));
        return mapper;
    }
}
