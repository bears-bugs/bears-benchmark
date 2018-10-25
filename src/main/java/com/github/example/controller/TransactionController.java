package com.github.example.controller;

import com.github.example.dto.request.CommandCreateTransaction;
import com.github.example.dto.response.TransactionData;
import com.github.example.model.Transaction;
import com.github.example.service.TransactionService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;
import java.net.URI;
import java.util.Collection;
import java.util.UUID;

@Controller("/api/1.0/transactions")
public class TransactionController extends AbstractController<Transaction, TransactionData> {

    private final TransactionService transactionService;

    @Inject
    public TransactionController(final ModelMapper modelMapper, final TransactionService transactionService) {
        super(modelMapper);
        this.transactionService = transactionService;
    }

    @Get
    @Produces
    public Collection<TransactionData> getAllTransactions() {
        return convertToDto(transactionService.getAll());
    }

    @Get(value = "/{transactionId}")
    @Produces
    public HttpResponse<TransactionData> getTransactionById(final UUID transactionId) {
        final Transaction transaction = transactionService.getById(transactionId);
        return HttpResponse.ok(convertToDto(transaction));
    }

    @Post
    @Consumes
    @Produces
    public HttpResponse<TransactionData> createTransaction(@Body final CommandCreateTransaction command, final HttpRequest request) {
        final Transaction transaction = transactionService.createBy(command);
        final URI location = HttpResponse.uri(request.getPath() + "/" + transaction.getId());
        return HttpResponse.accepted(location);
    }

    @Override
    protected Class<TransactionData> getDtoClass() {
        return TransactionData.class;
    }
}