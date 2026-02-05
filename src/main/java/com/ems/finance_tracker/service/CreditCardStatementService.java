package com.ems.finance_tracker.service;

import com.ems.finance_tracker.dto.CreditCardStatementDTO;
import com.ems.finance_tracker.exception.ResourceNotFoundException;
import com.ems.finance_tracker.model.entity.CreditCard;
import com.ems.finance_tracker.model.entity.CreditCardStatement;
import com.ems.finance_tracker.model.mapper.CreditCardStatementMapper;
import com.ems.finance_tracker.repository.CreditCardRepository;
import com.ems.finance_tracker.repository.CreditCardStatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCardStatementService {

    private final CreditCardStatementRepository creditCardStatementRepository;
    private final CreditCardStatementMapper creditCardStatementMapper;
    private final CreditCardRepository creditCardRepository;

    public List<CreditCardStatementDTO.Response> findAllCreditCardStatements() {
        return creditCardStatementRepository.findAll()
                .stream()
                .map(creditCardStatementMapper::toResponse)
                .toList();
    }

    public CreditCardStatementDTO.Response findCreditCardStatementById(Long id) {
        CreditCardStatement statement = findEntityById(id);

        return creditCardStatementMapper.toResponse(statement);
    }

    @Transactional
    public CreditCardStatementDTO.Response saveCreditCardStatement(CreditCardStatementDTO.Request dto) {
        CreditCard creditCard = creditCardRepository.findById(dto.creditCard().id())
                .orElseThrow(() -> new ResourceNotFoundException("Credit card not found. ID = " + dto.creditCard().id()));

        CreditCardStatement statement = creditCardStatementMapper.toEntity(dto, creditCard);

        return creditCardStatementMapper.toResponse(creditCardStatementRepository.save(statement));
    }

    @Transactional
    public CreditCardStatementDTO.Response updateCreditCardStatement(Long id, CreditCardStatementDTO.Update dto) {
        CreditCardStatement existingStatement = findEntityById(id);

        creditCardStatementMapper.updateEntity(existingStatement, dto);

        return creditCardStatementMapper.toResponse(creditCardStatementRepository.save(existingStatement));
    }

    @Transactional
    public void deleteCreditCardStatement(Long id) {
        CreditCardStatement statement = findEntityById(id);
        creditCardStatementRepository.delete(statement);
    }

    private CreditCardStatement findEntityById(Long id) {
        return creditCardStatementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credit card statement not found. ID = " + id));
    }
}
