package com.github.example.controller;

import com.github.example.dto.request.CommandCreateAccount;
import com.github.example.dto.response.AccountData;
import com.github.example.model.Account;
import com.github.example.service.AccountService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;
import java.net.URI;
import java.util.Collection;
import java.util.UUID;

@Controller("/api/1.0/accounts")
public class AccountController extends AbstractController<Account, AccountData> {

    private final AccountService accountService;

    @Inject
    public AccountController(final ModelMapper modelMapper, final AccountService accountService) {
        super(modelMapper);
        this.accountService = accountService;
    }

    @Get
    @Produces
    public Collection<AccountData> getAllAccounts() {
        return convertToDto(accountService.getAll());
    }

    @Get(value = "/{accountId}")
    @Produces
    public HttpResponse<AccountData> getAccountById(final UUID accountId) {
        final Account account = accountService.getById(accountId);
        return HttpResponse.ok(convertToDto(account));
    }

    @Post
    @Consumes
    @Produces
    public HttpResponse<AccountData> createAccount(@Body final CommandCreateAccount command, final HttpRequest request) {
        final Account account = accountService.createBy(command);
        final URI location = HttpResponse.uri(request.getPath() + "/" + account.getId());
        return HttpResponse.created(convertToDto(account), location);
    }

    @Override
    protected Class<AccountData> getDtoClass() {
        return AccountData.class;
    }
}