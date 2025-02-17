package strategy;

import dtos.Transaction;
import models.User;

import java.util.List;
import java.util.Map;

public interface SettleUpStrategy {
    public List<Transaction> settleUpUsers(Map<User, Integer> map) ;
}
