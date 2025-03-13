package ru.hpclab.hl.module1.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hpclab.hl.module1.dto.TicketDto;
import ru.hpclab.hl.module1.model.Ticket;

@Configuration
public class ModelMapperConfig {

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
