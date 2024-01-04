package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.dto.CurrencyDTO;
import com.ddevus.currencyExchange.entity.Currency;
import com.ddevus.currencyExchange.exceptions.WrapperException;
import com.ddevus.currencyExchange.services.interfaces.CurrencyService;
import com.ddevus.currencyExchange.utils.DtoEntityConvertor;

import java.util.ArrayList;
import java.util.List;

public class Currency_Service implements CurrencyService {

    private static final com.ddevus.currencyExchange.dao.interfaces.CurrencyDAO currencyDAO = CurrencyDAO.getINSTANCE();
    private static final CurrencyService INSTANCE = new Currency_Service();

    private Currency_Service() {}

    public static CurrencyService getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public CurrencyDTO save(CurrencyDTO currencyDTO) throws WrapperException {
        Currency currency = DtoEntityConvertor.convertCurrencyDtoToEntity(currencyDTO);
            currency = currencyDAO.save(currency);
            currencyDTO.setId(currency.getId());

            return currencyDTO;
    }

    @Override
    public CurrencyDTO findById(int id) throws WrapperException {
        var possibleCurrency = currencyDAO.findById(id);
        Currency possibleCurrencyEntity = possibleCurrency.get();
        CurrencyDTO currencyDTO = DtoEntityConvertor.convertCurrencyEntityToDto(possibleCurrencyEntity);

        return currencyDTO;
    }

    @Override
    public CurrencyDTO findByCode(String code) throws WrapperException {
        var possibleCurrency = currencyDAO.findByCode(code);
        Currency possibleCurrencyEntity = possibleCurrency.get();
        CurrencyDTO currencyDTO = DtoEntityConvertor.convertCurrencyEntityToDto(possibleCurrencyEntity);

        return currencyDTO;
    }

    @Override
    public List<CurrencyDTO> findAll() throws WrapperException {
        var currencyEntities = currencyDAO.findAll();
        var currencyDTOlist = new ArrayList<CurrencyDTO>();

        for (Currency currency : currencyEntities) {
            CurrencyDTO currencyDTO = DtoEntityConvertor.convertCurrencyEntityToDto(currency);
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
