package eu.burakkocak.vetsandpetsservice.api.dto;

import eu.burakkocak.vetsandpetsservice.data.model.Account;
import lombok.Data;

@Data
public class AccountHolder {
    private static Account account;
    public void set(Account account) {
        this.account = account;
    }
    public Account get() {
        return this.account;
    }
}
