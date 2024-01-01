package com.ddevus.currencyExchange.services;

import com.ddevus.currencyExchange.dao.CurrencyDAO;
import com.ddevus.currencyExchange.dao.CurrencyDAOImplementation;
import com.ddevus.currencyExchange.dto.CurrencyDTO;
import com.ddevus.currencyExchange.entity.CurrencyEntity;

import java.util.ArrayList;
import java.util.List;

public class CurrencyServiceImplementation implements CurrencyService{

    private static final CurrencyDAO currencyDAO = CurrencyDAOImplementation.getINSTANCE();
    private static final CurrencyService INSTANCE = new CurrencyServiceImplementation();

    private CurrencyServiceImplementation() {}

    public static CurrencyService getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public CurrencyDTO save(CurrencyDTO currencyDTO) {
        CurrencyEntity currencyEntity = convertCurrencyDtoToEntity(currencyDTO);

        try {
            currencyEntity = currencyDAO.save(currencyEntity);
            currencyDTO.setId(currencyEntity.getId());

            return currencyDTO;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CurrencyDTO findById(int id) {
        try {
            var possibleCurrency = currencyDAO.findById(id);
            CurrencyEntity possibleCurrencyEntity = possibleCurrency.get();
            CurrencyDTO currencyDTO = convertCurrencyEntityToDto(possibleCurrencyEntity);

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
            CurrencyDTO currencyDTO = convertCurrencyEntityToDto(possibleCurrencyEntity);

            return currencyDTO;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CurrencyDTO> findAll() {
        try {
            var currencyEntities = currencyDAO.findAll();
            var currencyDTOlist = new ArrayList<CurrencyDTO>();

            for (CurrencyEntity currencyEntity : currencyEntities) {
                CurrencyDTO currencyDTO = convertCurrencyEntityToDto(currencyEntity);
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

    private static CurrencyEntity convertCurrencyDtoToEntity(CurrencyDTO currencyDTO) {
        return new CurrencyEntity(currencyDTO.getId()
                , currencyDTO.getCode()
                , currencyDTO.getName()
                , currencyDTO.getSing());
    }

    private static CurrencyDTO convertCurrencyEntityToDto(CurrencyEntity currencyEntity) {
        return new CurrencyDTO(currencyEntity.getId()
                , currencyEntity.getName()
                , currencyEntity.getCode()
                , currencyEntity.getSing());
    }
}
