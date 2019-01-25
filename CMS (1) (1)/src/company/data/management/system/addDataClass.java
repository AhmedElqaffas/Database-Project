/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package company.data.management.system;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;


public class addDataClass {
    private dbConnector dbc = new dbConnector();
    protected String query;
    
    public  addDataClass(){
        query = "use cms2;";
        executeQuery();
    }
    
    protected boolean executeQuery() {
        try{
            dbc.st.executeUpdate(query);
            System.out.println("Executed Successfully:\n" + query);
            return true;
        }
        catch(SQLException e) {
            System.out.println("Could not Execute Query:\n" + query);
            return false;
        }
    }
    
    
}


class AddDataEmployee extends addDataClass {
    private String name;
    private String email;
    private String address;
    private String birthdate;
    private String department;
    private String role;
    private int salary;
    private ArrayList<String> phoneNum;

    public void setAddress(String address) {
        if(address.equals(""))
            this.address = null;
        else
            this.address = "'" + address + "'";
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setDepartment(String department) {
        if(department.equals(""))
            this.department = null;
        else
            this.department = "'" + department + "'";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNum(ArrayList<String> phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
    
    public boolean execute(){
        Calendar today = Calendar.getInstance();
        String date = today.get(Calendar.YEAR) + "-" + (today.get(Calendar.MONTH) + 1) + "-" + today.get(Calendar.DAY_OF_MONTH);
        
        String sal = null;
        if(salary > -1)
            sal = salary + "";
        
        query = "insert into employee(emp_name, salary, birth_date, employee_mail, employment_date, "
                + "dept_name, address) values ('"+name+"',"+sal+",'"+birthdate+"','"+email+"','"+date+"',"+department+","+address+");" ;
        boolean exe = executeQuery();
        if(!exe)
            return false;
        
        for(int i=0; i<phoneNum.size(); i++){
            if(phoneNum.get(i).equals("")) 
                continue;
            query = "insert into emp_phone(emp_id, phone) values((select max(emp_id) from employee), '"+phoneNum.get(i)+"');";
            executeQuery();
        }
        query = "insert into role_emp values((select max(emp_id) from employee), '"+role+"', '"+date+"', '1')";
        executeQuery();
        
        return true;
        
    }
    
}


class AddDataClient extends addDataClass {
    private String name;
    private String mail;
    private String city;
    private String phone;

    public void setCity(String city) {
        if(city.equals(""))
            this.city = null;
        else
            this.city = "'" + city + "'";
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        if(phone.equals(""))
            this.phone = null;
        else
            this.phone = "'" + phone + "'";
    }
    
    public void execute(){
        query = "insert into client(client_name, client_mail, client_phone, client_city) values('"+name+"', '"+mail+"', "+phone+", "+city+")";
       boolean exe= executeQuery();
       if(exe){
           JOptionPane.showMessageDialog(null,"Succesfully added client: " +"'" + name + "'");
       }else{
           JOptionPane.showMessageDialog(null,"Error! Please enter valid values");
       }
      
    }
    
}


class AddDataProject extends addDataClass {
    private String name;
    private int budget;
    private String startDate;
    private String dueDate;
    private int clientID;
    private ArrayList<String> depts;

    public void setName(String name) {
        this.name = name;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public void setDueDate(String dueDate) {
        if(dueDate.equals(""))
            this.dueDate = null;
        else    
            this.dueDate = "'" + dueDate + "'";
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setDepts(ArrayList<String> depts) {
        this.depts = depts;
    }
    
    
    public boolean execute(){
        String clientid = null;
        if(clientID != -1)
            clientid = clientID + "";
        
        String projBudget = null;
        if(budget != -1)
            projBudget = budget + "";
        
        query = "insert into project(proj_name, proj_budget, start_date, due_date, client_id)" +
                "values('"+name+"', "+projBudget+", '"+startDate+"', "+dueDate+", "+clientid+")";
        boolean exe = executeQuery();
        if(!exe)
            return false;
        
        for(int i=0; i<depts.size(); i++){
            if(depts.get(i).equals("---"))
                continue;
            query = "insert into proj_dept values((select max(proj_id) from project), '"+depts.get(i)+"')";
            executeQuery();
        }
        return true;
            
    }
    
    
}

class AddRequestData extends addDataClass {
    private String item;
    private String priority;
    private boolean arrived;
    private String department;
    private String supName;
    private int price;
    private String purchaseDate;
    

    public void setItem(String item) {
        this.item = item;
    }

    public void setPriority(String priority) {
        if(priority.equals(""))
            this.priority = null;
        else
            this.priority = "'" + priority + "'";
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSupName(String supName) {
        if(supName.equals(""))
            this.supName = null;
        else
            this.supName = "'" + supName + "'";
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPurchaseDate(String purchaseDate) {
        if(purchaseDate.equals(""))
            this.purchaseDate = null;
        else
            this.purchaseDate = "'" + purchaseDate + "'";
    }
    
    public void execute(){
        Calendar today = Calendar.getInstance();
        String date = today.get(Calendar.YEAR) + "-" + (today.get(Calendar.MONTH) + 1) + "-" + today.get(Calendar.DAY_OF_MONTH);
        
        String prc = null;
        if(price != -1)
            prc = price + "";
        
        query = "insert into needs(item, request_date, priority, arrived, dept_name, sup_name, price, purchase_date) " +
                "values('"+item+"', '"+date+"', "+priority+","+ arrived+", '"+department+"', "+supName+", "+prc+", "+purchaseDate+")";
        
        boolean exe=executeQuery();
        if(exe){
            JOptionPane.showMessageDialog(null, "Item: " +"'" + item +"'" + " has been added to the requests succesfully");
        }
        else{
            JOptionPane.showMessageDialog(null, "Error! please enter valid values");
        }
        
    }
}
