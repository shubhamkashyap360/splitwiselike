package services;

import dtos.Transaction;
import models.*;
import repositories.GroupRepository;
import repositories.UserExpenseRepository;
import strategy.HeapSettleUpStrategy;
import strategy.SettleUpStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private GroupRepository groupRepository;
    private UserExpenseRepository userExpenseRepository;
    private SettleUpStrategy settleUpStrategy;

    public UserService(GroupRepository groupRepository, UserExpenseRepository userExpenseRepository, SettleUpStrategy settleUpStrategy) {
        this.groupRepository = groupRepository;
        this.userExpenseRepository = userExpenseRepository;
        this.settleUpStrategy = settleUpStrategy;
    }

    public List<Transaction> settleUser(String userName, String groupName){


        /*
                1. Get All expenses of a group
                2. filter only regular expenses -> expenses
                3. for every expense we need to find user expenses
                4.
         */
        Map<User, Integer> extraAmountMap = new HashMap<>();

        List<Expense> expenses = groupRepository.findExpenseByGroup(groupName);

        for(Expense expense : expenses){
            if(expense.getExpenseType() == ExpenseType.REGULAR){
                List<UserExpense> userExpenseList = userExpenseRepository.findUserExpenseByExpense(expense.getDescription());
                for(UserExpense userExpense : userExpenseList){
                    User user = userExpense.getUser();
                    if (!extraAmountMap.containsKey(user)) {
                        extraAmountMap.put(user, 0);
                    }

                    Integer amount = extraAmountMap.get(user);
                    if (userExpense.getUserExpenseType() == UserExpenseType.PAID_BY) {
                        amount += userExpense.getAmount();
                    }else{
                        amount -= userExpense.getAmount();
                    }
                    extraAmountMap.put(user, amount);

                }
            }
        }
        List<Transaction> groupTransactions = settleUpStrategy.settleUpUsers(extraAmountMap);
        List<Transaction> userTransactions = new ArrayList<>();
        for(Transaction transaction : groupTransactions){
            if (transaction.getFrom().equals(userName) || transaction.getTo().equals(userName)) {
                userTransactions.add(transaction);
            }
        }
        return userTransactions;
    }
}
