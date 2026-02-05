package com.ems.finance_tracker.model.mapper;

import com.ems.finance_tracker.dto.CreditCardStatementDTO;
import com.ems.finance_tracker.model.entity.CreditCard;
import com.ems.finance_tracker.model.entity.CreditCardStatement;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class CreditCardStatementMapper {
    public CreditCardStatement toEntity(CreditCardStatementDTO.Request dto,
                                        CreditCard creditCard) {
        return CreditCardStatement.builder()
                .referenceMonth(YearMonth.parse(dto.referenceMonth()))
                .closingDay(creditCard.getClosingDay())
                .dueDay(creditCard.getDueDay())
                .creditCard(creditCard)
                .build();
    }

    public CreditCardStatementDTO.Response toResponse(CreditCardStatement statement) {
        return new CreditCardStatementDTO.Response(
                statement.getId(),
                statement.getReferenceMonth().toString(),
                statement.getClosingDay(),
                statement.getDueDay(),
                new CreditCardStatementDTO.CreditCardRef(
                        statement.getCreditCard() != null ? statement.getCreditCard().getId() : null,
                        statement.getCreditCard() != null ? statement.getCreditCard().getName() : null)
        );
    }

    public void updateEntity(CreditCardStatement statement, CreditCardStatementDTO.Update dto) {
        dto.closingDay().ifPresent(statement::setClosingDay);
        dto.dueDay().ifPresent(statement::setDueDay);
    }
}
