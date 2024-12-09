package com.example.API_Cinema.apis;


import com.example.API_Cinema.dto.BranchDTO;
import com.example.API_Cinema.exception.MethodArgumentTypeMismatchException;
import com.example.API_Cinema.service.impl.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/branch")
public class BranchApi {

    private final BranchService service;

    public BranchApi(BranchService service) {
        this.service = service;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createBranch(@Valid @RequestBody BranchDTO dto, BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessage = result
                                .getFieldErrors()
                                .stream()
                                .map(FieldError::getDefaultMessage)
                                .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            service.insert(dto);
            return ResponseEntity.status(201).body("Add branch successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateBranch(@Valid @RequestBody BranchDTO dto, BindingResult result){
        try{
            if(result.hasErrors()) {
                List<String> errorMessage = result
                        .getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            BranchDTO branch = service.update(dto);
            return ResponseEntity.status(200).body(branch);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{branchID}")
    public ResponseEntity<?> delete(@PathVariable String branchID) throws MethodArgumentTypeMismatchException {
        try {
            int id = Integer.parseInt(branchID);
            if (id < 0) {
                throw new MethodArgumentTypeMismatchException("Branch ID must be a positive integer");
            }
        } catch (NumberFormatException e) {
            throw new MethodArgumentTypeMismatchException("Branch ID must be an integer");
        }
        service.delete(Integer.parseInt(branchID));
        return ResponseEntity.status(200).body("Branch with ID " + branchID + " successfully removed");
    }
    @GetMapping("")
    public ResponseEntity<List<BranchDTO>> getAll(){
        List<BranchDTO> branchDTOList = service.getAll();
        return ResponseEntity.status(200).body(branchDTOList);
    }
    @GetMapping("/find")
    public ResponseEntity<?> findByKeyword(@RequestParam("title") String title, @RequestParam("keyword") String keyword) {
        try {
            if (keyword.equals("name")) {
                List<BranchDTO> branchDTOList = service.findByName(title);
                if(branchDTOList.isEmpty()){
                    return ResponseEntity.badRequest().body("Branch name does not exist in the database");
                }
                return ResponseEntity.status(200).body(branchDTOList);
            }
            if (keyword.equals("address")) {
                List<BranchDTO> branchDTOList = service.findByAddress(title);
                if(branchDTOList.isEmpty()){
                    return ResponseEntity.badRequest().body("Branch address does not exist in the database");
                }
                return ResponseEntity.status(200).body(branchDTOList);
            }
            if (keyword.equals("phone")) {
                List<BranchDTO> branchDTOList = service.findByPhone(title);
                if(branchDTOList.isEmpty()){
                    return ResponseEntity.badRequest().body("Branch phone does not exist in the database");
                }
                return ResponseEntity.status(200).body(branchDTOList);
            }
            if (keyword.equals("branchID")) {
                try {
                    int id = Integer.parseInt(title);
                    if (id < 0) {
                        throw new MethodArgumentTypeMismatchException("Branch ID must be a positive integer");
                    }
                } catch (NumberFormatException e) {
                    throw new MethodArgumentTypeMismatchException("Branch ID must be an integer");
                }
                BranchDTO branchDTO = service.findById(Integer.valueOf(title));
                return ResponseEntity.status(200).body(branchDTO);
            }
            throw new IllegalArgumentException("Invalid keyword: " + keyword + " required is branchID");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Title must be a valid input");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/find-branch-by-movie")
    private ResponseEntity<?> getBranchesThatShowTheMovie(@RequestParam("movieID") String movieId) throws MethodArgumentTypeMismatchException {
        try {
            int id = Integer.parseInt(movieId);
            if (id < 0) {
                throw new MethodArgumentTypeMismatchException("Movie ID must be a positive integer");
            }
        } catch (NumberFormatException e) {
            throw new MethodArgumentTypeMismatchException("Movie ID must be an integer");
        }

        List<BranchDTO> branchDTOList = service.getBranchThatShowTheMovie(Integer.parseInt(movieId));
        if (branchDTOList.isEmpty()) {
            return ResponseEntity.badRequest().body("Branch does not exist in the database");
        }
        return ResponseEntity.status(200).body(branchDTOList);
    }
}
