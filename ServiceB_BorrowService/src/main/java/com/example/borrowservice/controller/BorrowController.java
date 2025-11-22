package com.example.borrowservice.controller;

import com.example.borrowservice.dto.BorrowRequest;
import com.example.borrowservice.dto.ReturnRequest;
import com.example.borrowservice.model.BorrowRecord;
import com.example.borrowservice.service.BorrowDomainService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BorrowController {

    private final BorrowDomainService service;

    public BorrowController(BorrowDomainService service) {
        this.service = service;
    }

    @PostMapping("/borrow")
    public BorrowRecord borrow(@RequestBody BorrowRequest request) {
        return service.borrow(request.bookId(), request.memberId());
    }

    @PostMapping("/return")
    public BorrowRecord returnBook(@RequestBody ReturnRequest request) {
        return service.returnBook(request.recordId());
    }

    @GetMapping("/records")
    public List<BorrowRecord> findAll() {
        return service.listAll();
    }

    @GetMapping("/records/member/{memberId}")
    public List<BorrowRecord> findByMember(@PathVariable String memberId) {
        return service.listByMember(memberId);
    }
}
