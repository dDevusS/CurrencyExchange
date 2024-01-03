package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.interfaces.CurrencyDAO;
import com.ddevus.currencyExchange.dao.CurrencyDAOImplementation;
import com.ddevus.currencyExchange.dto.CurrencyDTO;
import com.ddevus.currencyExchange.entity.CurrencyEntity;
import com.ddevus.currencyExchange.exceptions.DatabaseException;
import com.ddevus.currencyExchange.exceptions.SQLBadRequestException;
import com.ddevus.currencyExchange.services.interfaces.CurrencyService;
import com.ddevus.currencyExchange.utils.DtoEntityConvertor;

import java.util.ArrayList;
import java.util.List;

public class CurrencyServiceImplementation implements CurrencyService {

    private static final CurrencyDAO currencyDAO = CurrencyDAOImplementation.getINSTANCE();
    private static final CurrencyService INSTANCE = new CurrencyServiceImplementation();

    private CurrencyServiceImplementation() {}

    public static CurrencyService getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public CurrencyDTO save(CurrencyDTO currencyDTO) throws DatabaseException, SQLBadRequestException {
        CurrencyEntity currencyEntity = DtoEntityConvertor.convertCurrencyDtoToEntity(currencyDTO);
            currencyEntity = currencyDAO.save(currencyEntity);
            currencyDTO.setId(currencyEntity.getId());

            return currencyDTO;
    }

    @Override
    public CurrencyDTO findById(int id) {
        try {
            var possibleCurrency = currencyDAO.findById(id);
            CurrencyEntity possibleCurrencyEntity = possibleCurrency.get();
            CurrencyDTO currencyDTO = DtoEntityConvertor.convertCurrencyEntityToDto(possibleCurrencyEntity);

            return currencyDTO;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CurrencyDTO findByCode(String code) {
        try {
            var possibleCurrency = currencyDAO.findByCode(code);
            CurrencyEntity possibleCurrencyEntity = possibleCurrency.get();
            CurrencyDTO currencyDTO = DtoEntityConvertor.convertCurrencyEntityToDto(possibleCurrencyEntity);

            return currencyDTO;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CurrencyDTO> findAll() throws DatabaseException {
        try {
            var currencyEntities = currencyDAO.findAll();
            var currencyDTOlist = new ArrayList<CurrencyDTO>();

            for (CurrencyEntity currencyEntity : currencyEntities) {
                CurrencyDTO currencyDTO = DtoEntityConvertor.convertCurrencyEntityToDto(currencyEntity);
                currencyDTOlist.add(currencyDTO);
            }

            return currencyDTOlist;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            var isDeleted = currencyDAO.delete(id);

            return isDeleted;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
