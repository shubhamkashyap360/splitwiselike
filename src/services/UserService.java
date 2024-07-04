package services;

import dtos.Transaction;
import models.Expense;
import models.ExpenseType;
import models.User;
import repositories.GroupRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private GroupRepository groupRepository;
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
                findUserExpensesByExpenseId(expense)
            }
        }
        return null;
    }
}
