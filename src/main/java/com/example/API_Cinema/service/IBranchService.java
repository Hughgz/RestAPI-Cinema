package com.example.API_Cinema.service;

import com.example.API_Cinema.dto.BranchDTO;
import com.example.API_Cinema.model.Branch;

import java.util.List;

public interface IBranchService {
    void insert(BranchDTO dto) throws Exception;
    BranchDTO update(BranchDTO dto);
    void delete(int id);
    List<BranchDTO> getAll();
    List<BranchDTO> findByName(String name);
    List<BranchDTO> findByAddress(String address);
    List<BranchDTO> findByPhone(String phone);
    BranchDTO findById(int id);
    BranchDTO convert(Branch branch);
    List<BranchDTO> getBranchThatShowTheMovie(int movieID);
}
