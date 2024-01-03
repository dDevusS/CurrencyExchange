package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAOImplementation;
import com.ddevus.currencyExchange.dao.interfaces.CurrencyDAO;
import com.ddevus.currencyExchange.dto.CurrencyDTO;
import com.ddevus.currencyExchange.entity.CurrencyEntity;
import com.ddevus.currencyExchange.exceptions.WrapperException;
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
    public CurrencyDTO save(CurrencyDTO currencyDTO) throws WrapperException {
        CurrencyEntity currencyEntity = DtoEntityConvertor.convertCurrencyDtoToEntity(currencyDTO);
            currencyEntity = currencyDAO.save(currencyEntity);
            currencyDTO.setId(currencyEntity.getId());

            return currencyDTO;
    }

    @Override
    public CurrencyDTO findById(int id) throws WrapperException {
        var possibleCurrency = currencyDAO.findById(id);
        CurrencyEntity possibleCurrencyEntity = possibleCurrency.get();
        CurrencyDTO currencyDTO = DtoEntityConvertor.convertCurrencyEntityToDto(possibleCurrencyEntity);

        return currencyDTO;
    }

    @Override
    public CurrencyDTO findByCode(String code) throws WrapperException {
        var possibleCurrency = currencyDAO.findByCode(code);
        CurrencyEntity possibleCurrencyEntity = possibleCurrency.get();
        CurrencyDTO currencyDTO = DtoEntityConvertor.convertCurrencyEntityToDto(possibleCurrencyEntity);

        return currencyDTO;
    }

    @Override
    public List<CurrencyDTO> findAll() throws WrapperException {
        var currencyEntities = currencyDAO.findAll();
        var currencyDTOlist = new ArrayList<CurrencyDTO>();

        for (CurrencyEntity currencyEntity : currencyEntities) {
            CurrencyDTO currencyDTO = DtoEntityConvertor.convertCurrencyEntityToDto(currencyEntity);
            currencyDTOlist.add(currencyDTO);
        }

        return currencyDTOlist;
    }

    @Override
    public boolean delete(int id) throws WrapperException {
            var isDeleted = currencyDAO.delete(id);

            return isDeleted;
    }
}
