package com.example.API_Cinema.service.impl;


import com.example.API_Cinema.dto.BranchDTO;
import com.example.API_Cinema.exception.ExistsDataException;
import com.example.API_Cinema.model.Branch;
import com.example.API_Cinema.repository.BranchRepo;
import com.example.API_Cinema.service.IBranchService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService implements IBranchService {
    private final BranchRepo repo;

    public BranchService(BranchRepo repo) {
        this.repo = repo;
    }


    @Override
    public void insert(BranchDTO dto) throws ExistsDataException {
        if(repo.existsByPhone(dto.getPhone())){
            throw new ExistsDataException("Phone already exists");
        }
        if(repo.existsByName(dto.getName())){
            throw new ExistsDataException("Name already exists");
        }
        if(repo.existsByAddress(dto.getAddress())){
            throw new ExistsDataException("Address already exists");
        }
        Branch branch = new ModelMapper().map(dto, Branch.class);
        repo.save(branch);
    }

    @Override
    public BranchDTO update(BranchDTO dto) {
        Branch currentBranch = repo.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Branch does not exits"));
        if(currentBranch != null){
            currentBranch.setName(dto.getName());
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
