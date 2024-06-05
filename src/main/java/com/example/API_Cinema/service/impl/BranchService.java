package com.example.API_Cinema.service.impl;


import com.example.API_Cinema.dto.BranchDTO;
import com.example.API_Cinema.model.Branch;
import com.example.API_Cinema.repo.BranchRepo;
import com.example.API_Cinema.service.IBranchService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService implements IBranchService {
    @Autowired
    BranchRepo repo;


    @Override
    public void insert(BranchDTO dto){
        Branch branch = new ModelMapper().map(dto, Branch.class);
        repo.save(branch);
    }

    @Override
    public BranchDTO update(BranchDTO dto) {
        Branch currentBranch = repo.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Branch does not exits"));
        if(currentBranch != null){
            currentBranch.setName(dto.getName());
            currentBranch.setImgURL(dto.getImgURL());
            currentBranch.setPhone(dto.getPhone());
            currentBranch.setAddress(dto.getAddress());
            repo.save(currentBranch);
        }
        return convert(currentBranch);
    }

    @Override
    @Transactional
    public void delete(int id) {
        repo.deleteById(id);
    }
    @Override
    public List<BranchDTO> getAll() {
        List<Branch> branchList = repo.findAll();
        return branchList.stream().map(branch -> convert(branch)).collect(Collectors.toList());
    }

    @Override
    public List<BranchDTO> findByName(String name) {
        List<Branch> branchList = repo.findByName(name);
        return branchList.stream().map(branch ->convert(branch)).collect(Collectors.toList());
    }

    @Override
    public List<BranchDTO> findByAddress(String address) {
        List<Branch> branchList = repo.findByAddress(address);
        return branchList.stream().map(branch ->convert(branch)).collect(Collectors.toList());
    }

    @Override
    public List<BranchDTO> findByPhone(String phone) {
        List<Branch> branchList = repo.findByPhone(phone);
        return branchList.stream().map(branch ->convert(branch)).collect(Collectors.toList());
    }

    @Override
    public BranchDTO findById(int id) {
        Branch branch = repo.findById(id).orElseThrow(()-> new RuntimeException("Branch does not exits"));
        return convert(branch);
    }


    @Override
    public BranchDTO convert(Branch branch) {
        return new ModelMapper().map(branch, BranchDTO.class);
    }

    @Override
    public List<BranchDTO> getBranchThatShowTheMovie(int movieID) {
        List<Branch> branchList = repo.getBranchThatShowTheMovie(movieID);
        return branchList.stream().map(branch ->convert(branch)).collect(Collectors.toList());
    }
}
