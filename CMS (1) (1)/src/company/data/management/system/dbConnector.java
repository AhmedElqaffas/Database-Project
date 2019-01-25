package company.data.management.system;

import java.awt.Component;
import java.awt.Rectangle;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
//Useless code:
//class DatabaseDate{
//    private int day;
//    private int month;
//    private int year;
//    
//    public DatabaseDate(int day, int month, int year){
//        this.day = day;
//        this.month = month;
//        this.year = year;
//    }
//    
//    public String getDateString(){
//        return Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
//    }
//}

public class dbConnector {

    //Thesse elements are static because they will never change with time
    public static Connection con;
    public static Statement st, st2;
    public static ResultSet rs;

    //Here we initialize static elements (Note the the normal initializer 
    //method was not used because it won't initialize static elements 
    static {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            //Add local host port number after "localhost:"
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms2?autoReconnect=true&useSSL=false", "root", "");
            st = con.createStatement();
            st2 = con.createStatement();

        } catch (Exception e) {
            System.out.println(e);
        }

        /*String query = "create view roles as select * from role;";
         try{
             st.executeUpdate(query);
             
        }
        catch(Exception e){
            System.out.println(e);
        }*/
    }

    public static void insertDepartment(String name, String budget, String head, String date) {
        if(name.equals("")){
            JOptionPane.showMessageDialog(null,"Error! Please Department Name");
            return;
        }
        String sqlInsert = "insert into department(dept_name";
        if (!budget.equals("")) {
            sqlInsert += ", dept_budget";
        }
        if (!head.equals("")) {
            sqlInsert += ", head";
            sqlInsert += ", head_date";
        }
        date = "CURRENT_TIME";

        sqlInsert += ") ";
        sqlInsert += "VALUES ('" + name + "'";
        if (!budget.equals("")) {
            sqlInsert += ", " + budget;
        }
        if (!head.equals("")) {
            sqlInsert += ", " + head;
            sqlInsert += ", " + date;
        }

        sqlInsert += ");";

        System.out.println("The SQL query is: " + sqlInsert);  // Echo for debugging
        try {
            int InsertCount = st.executeUpdate(sqlInsert);
            JOptionPane.showMessageDialog(null,"Department " + "'" +name+"'"+" has been added successfully");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error! Please enter valid values");
        }
    }

    public static JTable searchDepartment(String departmentName) {

        String strSelect = "select * from department where 1 = 1 ";
        if (!departmentName.equals("")) {
            strSelect += "and dept_name =\""  + departmentName  + "\";";
        }

        System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();
        try {
            ResultSet rs = st.executeQuery(strSelect);
            while (rs.next()) {   // Move the cursor to the next row
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(rs.getString("dept_name"));
                temp.add(Integer.toString(rs.getInt("dept_budget")));
                temp.add(Integer.toString(rs.getInt("head")));
                temp.add(rs.getString("head_date"));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    System.out.println(rsString.get(i).get(j));
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.size(); j++) {
                    if (i != j && rsString.get(i).get(0) == rsString.get(j).get(0)) {
                        System.out.println("In");
                        rsString.remove(i);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in department search");
        }
        return General.createCustomTable(rsString, "department");

    }

    public static void deleteDepartment(String deptName) {
        if (!deptName.equals("")) {
            String query = "delete from department where " + "dept_name = " + "'" + deptName + "'";

            try {
                System.out.println(query);
                st.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Department: " +"'" + deptName +"'" + " has been deleted successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error! Invalid data entry");
            }
        }
    }

    public static void modifyDepartment(String departmentName, String newBudget, String newHead, String newHeadDate) {

        if (departmentName.equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter department name");
            return;
        }
        String query = "UPDATE department SET ";
        if (!newBudget.equals("")) {
            query += "dept_budget = " + newBudget + ",";
        }
        if (!newHead.equals("")) {
            query += "head = " + newHead + ",";
        }
        if (!newHead.equals("") && newHeadDate.equals("")) {
            query += "head_date = " + "CURRENT_TIME" + ",";
        } else if (!newHeadDate.equals("")) {
            query += "head_date = '" + newHeadDate + "',";
        }
        query = query.substring(0, query.length() - 1) + " ";
        query += "WHERE 1 = 1 ";
        if (!departmentName.equals("")) {
            query += "and dept_name = '"  + departmentName  + "';";
        }

        System.out.println("The SQL query is: " + query);

        try {
            int InsertCount = st.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Department: " + "'" + departmentName + "'" +" has been modified successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error! Please enter valid values");
        }
        //String queryTemp = "UPDATE department SET dept_budget = value1, head = value2, head_date = value2 WHERE condition;";
    }

    public static JTable searchEmployee(String name, String id) {
        String strSelect = "select employee.emp_id, emp_name, salary, birth_date, employee_mail, employment_date, dept_name, address, role_name, role_date, active,group_concat(distinct(phone)) as phone from employee left join role_emp ON employee.emp_id = role_emp.emp_id "
                + " left join emp_phone on employee.emp_id = emp_phone.emp_id where 1 = 1 ";
        if (!name.equals("")) {
            strSelect += "and emp_name = '" +name + "'";
        }
        if (!id.equals("")) {
            strSelect += " and employee.emp_id = " + id;
        }
        strSelect += " and active = 1 group by emp_id ";

        System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();
        try {
            ResultSet rs = st.executeQuery(strSelect);
            System.out.println("Query entry is successful");
            while (rs.next()) {   // Move the cursor to the next row
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(Integer.toString(rs.getInt("emp_id")));
                temp.add(rs.getString("emp_name"));
                temp.add(Integer.toString(rs.getInt("salary")));
                Calendar today = Calendar.getInstance();
                int date = today.get(Calendar.YEAR);
                String age = (date - Integer.parseInt(rs.getString("birth_date").substring(0, 4))) + "";
                temp.add(age);
                temp.add(rs.getString("employee_mail"));
                temp.add(rs.getString("employment_date"));
                temp.add(rs.getString("dept_name"));
                temp.add(rs.getString("address"));
                temp.add(rs.getString("role_name"));
                temp.add(rs.getString("role_date"));
                temp.add(rs.getString("phone"));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    //System.out.println(rsString.get(i).get(j));
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }

//            for (int i = 0; i < rsString.size(); i++){
//                for (int j = 0; j < rsString.get(i).size(); j++){
//                    System.out.println(rsString.get(i).get(j));
//                }
//            }
        } catch (SQLException ex) {
            System.out.println("Error in employee search");
        }
        return General.createCustomTable(rsString, "employee");
    }

    public static void deleteEmployee(String name, String id) {
        int eid = 0;
        if (!id.equals("")) {
            try {
                eid = Integer.parseInt(id);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please enter numeric ID!");
                return;
            }
        }
        if (id.equals("") && name.equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter name,id or both");
            return;
        }
        String query = "delete from employee where 1 = 1 ";

        if (!name.equals("")) {
            query += "and emp_name = " + "'" + name + "'";
        }
        if (!id.equals("")) {
            query += "and emp_id = " + id;
        }
        try {
            System.out.println("The SQL query is:" + query);
            st.executeUpdate(query);
            if(!name.equals(""))
            JOptionPane.showMessageDialog(null,"Employee: " + "'" + name +"'" + " has been deleted successfully");
            else
            JOptionPane.showMessageDialog(null,"Employee with id: " + "'" + id +"'" + " has been deleted successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error! deletion of employee failed!");
        }

    }

    public static void modifyEmployee(String name, String id, String newName, String newSalary, String newBirthDate, String newEmail, String newEmploymentDate, String newDepartmentName, String newAddress, String newRole, String phone) {
        int eid = 0;
        try{
            if (!phone.equals("")){
            int test=Integer.parseInt(phone);
            }
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Please enter numeric phone number!");
            return;
        }
        if (!id.equals("")) {
            try {
                eid = Integer.parseInt(id);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please enter numeric ID!");
                return;
            }
        }
        if (id.equals("") && name.equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter name,id or both");
            return;
        }
        if (newName.equals("") && newSalary.equals("") && newBirthDate.equals("") && newEmail.equals("") && newEmploymentDate.equals("") && newDepartmentName.equals("") && newAddress.equals("") && newRole.equals("---") && phone.equals("")){
            JOptionPane.showMessageDialog(null, "Please enter a value to modify");
            return;
        }
        String query = "UPDATE employee SET ";
        ArrayList<String> roleQueriesTwo = new ArrayList<String>();
        if (!newName.equals("")) {
            query += "emp_name = '" + newName + "',";
        }
        if (!newSalary.equals("")) {
            query += "salary = " + newSalary + ",";
        }
        if (!newBirthDate.equals("")) {
            query += "birth_date = '" + newBirthDate + "',";
        }
        if (!newEmail.equals("")) {
            query += "employee_mail = '" + newEmail + "',";
        }
        if (!newEmploymentDate.equals("")) {
            query += "employment_date = '" + newEmploymentDate + "',";
        }
        if (!newDepartmentName.equals("")) {
            query += "dept_name = '" + newDepartmentName + "',";
        }
        if (!newAddress.equals("")) {
            query += "address = '" + newAddress + "',";
        }

        query = query.substring(0, query.length() - 1) + " ";
        query += "WHERE 1 = 1 ";
        if (!name.equals("")) {
            query += "and emp_name = \"" + name + "\"";
        }
        if (!id.equals("")) {
            query += "and emp_id = " + id;
        }
        System.out.println("The SQL query is: " + query);
        System.out.println(query);
        try{
        int InsertCount = st.executeUpdate(query);
        System.out.println("Query entry is successful");
        }
        catch (Exception ex){
            System.out.println("Problem with employee modify (No roles)");
        }

        if (!newRole.equals("---")) {
            String roleQueryOne;
            roleQueryOne = "update role_emp set active = 0 where emp_id in (select emp_id from employee where 1 = 1 ";
            if (!name.equals("")) {
                roleQueryOne += "and emp_name = '"  +name + "'";
            }
            if (!id.equals("")) {
                roleQueryOne += "and emp_id = " + id;
            }
            roleQueryOne += ")";

            String strSelect = "select * from employee l where 1 = 1 ";
            if (!name.equals("")) {
                strSelect += "and emp_name ='"  +name +"'";
            }
            if (!id.equals("")) {
                strSelect += "and emp_id = " + id;
            }

            System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
            ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();
            try {
                ResultSet rs = st.executeQuery(strSelect);
                System.out.println("Query entry is successful");
                while (rs.next()) {   // Move the cursor to the next row
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(Integer.toString(rs.getInt("emp_id")));
                    temp.add(rs.getString("emp_name"));
                    temp.add(Integer.toString(rs.getInt("salary")));
                    temp.add(rs.getString("birth_date"));
                    temp.add(rs.getString("employee_mail"));
                    temp.add(rs.getString("employment_date"));
                    temp.add(rs.getString("dept_name"));
                    temp.add(rs.getString("address"));
                    rsString.add(temp);
                }
                for (int i = 0; i < rsString.size(); i++) {
                    for (int j = 0; j < rsString.get(i).size(); j++) {
                        System.out.println(rsString.get(i).get(j));
                        if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                            rsString.get(i).set(j, "");
                        }
                    }
                }

                for (int i = 0; i < rsString.size(); i++) {
                    String tempQuery = "insert into role_emp values(";
                    tempQuery += rsString.get(i).get(0) + ", ";
                    tempQuery += "'" + newRole + "', ";
                    tempQuery += "CURRENT_DATE" + ", ";
                    tempQuery += "1)";
                    roleQueriesTwo.add(tempQuery);
                }
                System.out.println(roleQueryOne);
                int InsertCount2 = st.executeUpdate(roleQueryOne);
                System.out.println("Query entry is successful");
                for (int i = 0; i < roleQueriesTwo.size(); i++) {
                System.out.println(roleQueriesTwo.get(i));
                int InsertCount3 = st.executeUpdate(roleQueriesTwo.get(i));
                System.out.println("Query entry is successful");
                }
//            for (int i = 0; i < rsString.size(); i++){
//                for (int j = 0; j < rsString.get(i).size(); j++){
//                    System.out.println(rsString.get(i).get(j));
//                }
//            }
            } catch (SQLException ex) {
               JOptionPane.showMessageDialog(null,"Error! invalid data entry1");
               return;

            }

        }
        if (!phone.equals("")) {
            String pquery = "insert into emp_phone values(" + eid + ",'" + phone + "')";
            try {
                st2.executeUpdate(pquery);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error! check entered employee data");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Employee has been modified successfully!");
    }

    public static JTable searchRequest(String id) throws Exception {
        String strSelect = "select request_id,item,request_date,priority,arrived,dept_name,sup_name,price,purchase_date,"
                + "(select DATEDIFF(purchase_date,request_date))as delay from needs where 1=1 ";

        if (!id.equals("")) {
            strSelect += "and request_id = " + id + ";";
        }

        System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();
        try {
            ResultSet rs = st.executeQuery(strSelect);
            System.out.println("Query entry is successful");

            while (rs.next()) {   // Move the cursor to the next row
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(Integer.toString(rs.getInt("request_id")));
                temp.add(rs.getString("item"));
                temp.add(rs.getString("request_date"));
                temp.add(rs.getString("priority"));
                temp.add(Boolean.toString(rs.getBoolean("arrived")));
                temp.add(rs.getString("dept_name"));
                temp.add(rs.getString("sup_name"));
                temp.add(Integer.toString(rs.getInt("price")));
                temp.add(rs.getString("purchase_date"));
                temp.add(rs.getString("delay"));
                rsString.add(temp);
            }
            return General.createCustomTable(rsString, "needs");
        } catch (Exception ex) {
            throw new Exception("Error in search");

        }

    }

    public static void deleteRequest(String id) {
        String query = "";
        if (!id.equals("")) {
            query += "delete from needs where request_id =" + id + ";";
        }

        try {
            System.out.println("The SQL query is:" + query);
            st.executeUpdate(query);
            System.out.println("Query entry is successful");
        } catch (Exception e) {
            System.out.println("Error in request delete");
        }

    }

    public static void modifyRequest(String id, String newName, String priority, String department, boolean arrived, String supName, String price, String date) throws Exception {
        String query = "UPDATE needs SET ";
         int rid;
        try {
            rid = Integer.parseInt(id);
        } catch (Exception e) {
           throw new Exception("Please Enter a Numeric ID!");
        }
        
        if (!date.equals("")){
                String query2;
                ResultSet rs = null;
                String reqDate="";
            if(!id.equals("")){
                
                try {
                    query2 = "Select request_date from needs where request_id= " + rid;
                    rs = st.executeQuery(query2);

                } catch (Exception es) {

                }
                    try {
                        rs.next();
                        reqDate = "" + rs.getString(1);
                    } catch (SQLException ex) { 
                    }
            }
          
            boolean check= compareDate(reqDate,date);
            if(!check){
                throw new Exception("Purchase Date Can't Precede Request Date");
               
            }
                
        }
        
        if (!newName.equals("")) {
            query += "item = '" + newName + "',";
        }
        if (!priority.equals("--")) {
            query += "priority = '" + priority + "',";
        }

        query += "arrived= " + arrived + ",";
        String query2;
        ResultSet rs;
        try {
            query2 = "Select sup_name from needs where request_id= " + id;
            rs = st.executeQuery(query2);

        } catch (Exception es) {
            throw new Exception("Please enter a valid id");
        }
        rs.next();
        String first = "" + rs.getString(1);
        System.out.println(first);
        if (arrived && (supName.equals("") || price.equals("") || date.equals("")) && first.equals("null")) {
            JOptionPane.showMessageDialog(null, "Insert Purchasing info");
            throw new Exception("Insert Purchasing info");
        }

        int price2;
        if (!price.equals("")) {
            if (first.equals("null") && (date.equals("") || supName.equals(""))) {
                throw new Exception("Please enter full purchase details");
            }
            try {
                price2 = Integer.parseInt(price);
                query += "price = " + price2 + ",";

            } catch (Exception e) {
                throw new Exception("Price Error");
            }

        }

        if (!date.equals("")) {
            System.out.println(first);
            if (first.equals("null") && (supName.equals("") || price.equals(""))) {
                System.out.println(first);
                throw new Exception("Please enter full purchase details");
            }
            query += "purchase_date = '" + date + "',";
        }
        if (!department.equals("")) {
            query += "dept_name = '" + department + "',";
        }
        if (!supName.equals("")) {
            if (first.equals("null") && (date.equals("") || price.equals(""))) {
                throw new Exception("Please enter full purchase details");
            }
            query += "sup_name = '" + supName + "',";
        }
        query = query.substring(0, query.length() - 1) + " ";
        query += "WHERE request_id =  " + id;
        System.out.println("The SQL query is: " + query);

        try {
            int InsertCount = st.executeUpdate(query);
            System.out.println("Query entry is successful");
        } catch (SQLException ex) {
            System.out.println("Error in request modify");
        }
    }

    public static JTable searchProject(String name, String id) {
        String strSelect = "select project.proj_id, proj_name, proj_budget, start_date, due_date, client_id, manager ,GROUP_CONCAT(DISTINCT(dept_name)) as dept_name from project left outer join proj_dept on project.proj_id=proj_dept.proj_id where 1 = 1 ";
        if (!name.equals("")) {
            strSelect += "and project.proj_name= '" + name  + "'";
        }
        if (!id.equals("")) {
            strSelect += "and project.proj_id = " + id;
        }
        strSelect+=" Group by proj_id";
        System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();
        try {
            ResultSet rs = st.executeQuery(strSelect);
            System.out.println("Query entry is successful");
            while (rs.next()) {   // Move the cursor to the next row
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(Integer.toString(rs.getInt("proj_id")));
                temp.add(rs.getString("proj_name"));
                temp.add(Integer.toString(rs.getInt("proj_budget")));
                temp.add(rs.getString("start_date"));
                temp.add(rs.getString("due_date"));
                temp.add(Integer.toString(rs.getInt("client_id")));
                temp.add(Integer.toString(rs.getInt("manager")));
                temp.add(rs.getString("dept_name"));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    System.out.println(rsString.get(i).get(j));
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
//            for (int i = 0; i < rsString.size(); i++){
//                for (int j = 0; j < rsString.get(i).size(); j++){
//                    System.out.println(rsString.get(i).get(j));
//                }
//            }
        } catch (SQLException ex) {
            System.out.println("Error in project search");
        }
        return General.createCustomTable(rsString, "project");
    }

    public static void deleteProject(String name, String id) {
        String query = "delete from project where 1 = 1 ";

        if (!name.equals("")) {
            query += "and proj_name = " + "'" + name + "'";
        }
        if (!id.equals("")) {
            query += "and proj_id = " + id;
        }
        try {
            System.out.println("The SQL query is:" + query);
            st.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Project(s) deletion done!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error! Invalid data entry");
        }

    }

    public static void modifyProject(String name, String id, String newName, String newBudget, String newStartDate, String newDueDate, String newClientID, String department) throws Exception {
        boolean exec = true;
        int pid;
         if(!id.equals("")){
            
            try {
                pid = Integer.parseInt(id);
            } catch (Exception e) {
               throw new Exception("Please Enter a Numeric ID!");
            }
         }
        
        if(newStartDate.equals("") || newDueDate.equals("")){
            if (!newStartDate.equals("")){
                String query2;
                ResultSet rs = null;
                String date="";
            if(!id.equals("")){
                
                try {
                    query2 = "Select due_date from project where proj_id= " + id;
                    rs = st.executeQuery(query2);

                } catch (Exception es) {

                }
                    try {
                        rs.next();
                        date = "" + rs.getString(1);
                    } catch (SQLException ex) { 
                    }
            }
            else if(!name.equals("")){
                
                try {
                    query2 = "Select due_date from project where proj_name= '" + name+"'";
                    rs = st.executeQuery(query2);

                } catch (Exception es) {

                }
                    try {
                        rs.next();
                        date = "" + rs.getString(1);
                    } catch (SQLException ex) { 
                    }
            }
            boolean check= compareDate(newStartDate,date);
            if(!check){
                throw new Exception("Start Date Can't Exceed Due Date");
               
            }
                
        }
         
         
         
         
         
         if (!newDueDate.equals("")){
                String query2;
                ResultSet rs = null;
                String date="";
            if(!id.equals("")){
                
                try {
                    query2 = "Select start_date from project where proj_id= " + id;
                    rs = st.executeQuery(query2);

                } catch (Exception es) {

                }
                    try {
                        rs.next();
                        date = "" + rs.getString(1);
                    } catch (SQLException ex) { 
                    }
            }
            else if(!name.equals("")){
                
                try {
                    query2 = "Select start_date from project where proj_name= '" + name+"'";
                    rs = st.executeQuery(query2);

                } catch (Exception es) {

                }
                    try {
                        rs.next();
                        date = "" + rs.getString(1);
                    } catch (SQLException ex) { 
                    }
            }
            boolean check= compareDate(date,newDueDate);
            if(!check){
                throw new Exception("Start Date Can't Exceed Due Date");
            }
                
        }
            
        }
         
        
        
        
        String query = "UPDATE project SET ";
        if (!newName.equals("")) {
            query += "proj_name = '" + newName + "',";
        }
        if (!newBudget.equals("")) {
            query += "proj_budget = " + newBudget + ",";
        }
        if (!newStartDate.equals("")) {
            query += "start_date = '" + newStartDate + "',";
        }
        if (!newDueDate.equals("")) {
            query += "due_date = '" + newDueDate + "',";
        }
        /*if (!newState.equals("")) {
            query += "state = '" + newState + "',";
        }*/
        if (!newClientID.equals("")) {
            query += "client_id = '" + newClientID + "',";
        }
        query = query.substring(0, query.length() - 1) + " ";
        query += "WHERE 1 = 1 ";
        if (!name.equals("")) {
            query += "and proj_name = \""  +name + "\" ";
        }
        if (!id.equals("")) {
            query += "and proj_id = " + id + ";";
        }
        
        System.out.println("The SQL query is: " + query);

        try {
            int InsertCount = st.executeUpdate(query);
            System.out.println("Query entry is successful");

        } catch (SQLException ex) {
            if(department.equals(""))
                throw new Exception("Error in project modification");

        }

        if (!department.equals("")) {
            try {
                if (name.equals("") && id.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter project id or name");
                } else if (!name.equals("") && id.equals("")) {
                    String query2 = "select proj_id from project where proj_name= " + "'" + name + "'";
                    rs = st2.executeQuery(query2);
                    while (rs.next()) {
                        String query3 = "replace into proj_dept values(" + Integer.parseInt(rs.getString("proj_id")) + ",'" + department + "')";
                        Statement st3 = con.createStatement();
                        st3.executeUpdate(query3);
                    }
                    JOptionPane.showMessageDialog(null, "New Department added successfully!");
                } else if (!name.equals("") && id.equals("")) {
                    String query2 = "select proj_id from project where proj_name= " + "'" + name + "'";
                    rs = st2.executeQuery(query2);
                    while (rs.next()) {
                        if (Integer.parseInt(id) == Integer.parseInt(rs.getString("proj_id"))) {
                            String query3 = "replace into proj_dept values(" + Integer.parseInt(rs.getString("proj_id")) + ",'" + department + "')";
                            Statement st3 = con.createStatement();
                            st3.executeUpdate(query3);
                        }
                    }
                    JOptionPane.showMessageDialog(null, "New Department added successfully!");
                } else {
                    String query3 = "replace into proj_dept values(" + Integer.parseInt(id) + ",'" + department + "')";
                    Statement st3 = con.createStatement();
                    st3.executeUpdate(query3);
                    JOptionPane.showMessageDialog(null, "New Department added successfully!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please check project id and department name!");
            }
        }

    }

    public static JTable searchClient(String name, String id) {
        String strSelect = "select * from client where 1 = 1 ";
        if (!name.equals("")) {
            strSelect += "and client_name = '"  + name  + "'";
        }
        if (!id.equals("")) {
            strSelect += "and client_id = " + id;
        }

        System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();
        try {
            ResultSet rs = st.executeQuery(strSelect);
            System.out.println("Query entry is successful");
            while (rs.next()) {   // Move the cursor to the next row
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(Integer.toString(rs.getInt("client_id")));
                temp.add(rs.getString("client_name"));
                temp.add(rs.getString("client_mail"));
                temp.add(rs.getString("client_phone"));
                temp.add(rs.getString("client_city"));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    System.out.println(rsString.get(i).get(j));
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
//            for (int i = 0; i < rsString.size(); i++){
//                for (int j = 0; j < rsString.get(i).size(); j++){
//                    System.out.println(rsString.get(i).get(j));
//                }
//            }
        } catch (SQLException ex) {
            System.out.println("Error in Client search");
        }
        return General.createCustomTable(rsString, "client");
    }

    public static void deleteClient(String name, String id) {
        String strDelete = "delete from client where 1=1 ";
        if (!name.equals("")) {
            strDelete += "and client_name= \"" + name + "\"";
        }
        if (!id.equals("")) {
            strDelete += "and client_id= \"" + id + "\"";
        }
        try {
            st.executeUpdate(strDelete);
            System.out.println("The SQL query is: " + strDelete);  // Echo For debugging
        } catch (SQLException ex) {
            System.out.println("Error in Deleting Client");
        }
    }

    public static void modifyClient(String name, String id, String newName, String newMail, String newPhone, String newCity) {
        boolean in = false;
        if (!name.equals("") || !id.equals("")) {
            String strModify = "UPDATE client SET ";
            if (!newName.equals("")) {
                in = true;
                strModify += "client_name ='" + newName + "'";
            }
            if (!newMail.equals("")) {
                if (in) {
                    strModify += ",client_mail ='" + newMail + "'";
                } else {
                    in = true;
                    strModify += "client_mail ='" + newMail + "'";
                }
            }
            if (!newPhone.equals("")) {
                if (in) {
                    strModify += ",client_phone ='" + newPhone + "'";
                } else {
                    in = true;
                    strModify += "client_phone ='" + newPhone + "'";
                }
            }
            if (!newCity.equals("")) {
                if (in) {
                    strModify += ",client_city ='" + newCity + "'";
                } else {
                    strModify += "client_city ='" + newCity + "'";
                }
            }
            strModify += " WHERE 1=1";

            if (!name.equals("")) {
                strModify += " and client_name = '" + name + "'";
            }
            if (!id.equals("")) {
                strModify += " and client_id ='" + id + "'";
            }
            try {
                st.executeUpdate(strModify);
                System.out.println("The SQL query is: " + strModify);  // Echo For debugging
                JOptionPane.showMessageDialog(null, "Client modified successfully!");
            } catch (SQLException ex) {
                System.out.println("The: " + strModify);  // Echo For debugging

                JOptionPane.showMessageDialog(null, "Error! invalid data entry");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please Enter Values in Name or ID");
        }

    }

    public static void addSupplier(String name, String email, String phone, String city) {
        String strInsert1 = "insert into supplier (";
        String strInsert2 = " VALUES (";
        if (!name.equals("")) {
            strInsert1 += "sup_name";
            strInsert2 += "'" + name + "'";
        }
        if (!city.equals("")) {
            strInsert1 += ",sup_city";
            strInsert2 += "," + "'" + city + "'";
        }
        if (!email.equals("")) {
            strInsert1 += ",sup_mail";
            strInsert2 += "," + "'" + email + "'";
        }
        if (!phone.equals("")) {
            strInsert1 += ",sup_phone)";
            strInsert2 += ",'" + phone + "')";
        } else {
            strInsert1 += ")";
            strInsert2 += ")";
        }
        String Query = strInsert1 + strInsert2;
        System.out.println(Query);
        try {
            st.executeUpdate(Query);
            System.out.println("The SQL query is: " + Query);  // Echo For debugging
            JOptionPane.showMessageDialog(null, "Supplier submitted succesfully");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error Adding supplier");
        }

    }

    public static JTable searchSupplier(String name) {

        String strSelect = "select * from supplier where 1=1 ";
        if (!name.equals("")) {
            strSelect += "and sup_name = '" + name + "'";
        }
        System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();

        try {
            ResultSet rs = st.executeQuery(strSelect);
            System.out.println("Query entry is successful");
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(rs.getString("sup_name"));
                temp.add(rs.getString("sup_city"));
                temp.add(rs.getString("sup_mail"));
                temp.add(rs.getString("sup_phone"));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    System.out.println(rsString.get(i).get(j));
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in Supplier Search");

        }
        return General.createCustomTable(rsString, "supplier");

    }

    public static void deleteSupplier(String name) {
        if (!name.equals("")) {
            String strDelete = "delete from supplier where 1=1 ";
            strDelete += "and sup_name= \"" + name + "\"";
            try {
                st.executeUpdate(strDelete);
                System.out.println("The SQL query is: " + strDelete);  // Echo For debugging
            } catch (SQLException ex) {
                System.out.println("Error in Deleting Supplier");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please Enter Name in TextFields");
        }

    }

    public static void modifySupplier(String name, String newName, String newCity, String newMail, String newPhone) {
        boolean in = false;
        if (!name.equals("")) {
            String strModify = "UPDATE supplier SET ";
            if (!newName.equals("")) {
                in = true;
                strModify += "sup_name ='" + newName + "'";
            }
            if (!newCity.equals("")) {
                if (in) {
                    strModify += ",sup_City ='" + newCity + "'";
                } else {
                    in = true;
                    strModify += "sup_City ='" + newCity + "'";
                }
            }
            if (!newMail.equals("")) {
                if (in) {
                    strModify += ",sup_Mail ='" + newMail + "'";
                } else {
                    in = true;
                    strModify += "sup_Mail ='" + newMail + "'";

                }
            }
            if (!newPhone.equals("")) {
                if (in) {
                    strModify += ",sup_phone ='" + newPhone + "'";
                } else {
                    strModify += "sup_phone ='" + newPhone + "'";
                }
            }
            strModify += " WHERE sup_name= '" + name + "'";

          
            System.out.println("The: " + strModify);  // Echo For debugging

            try {
                st.executeUpdate(strModify);
                JOptionPane.showMessageDialog(null, "Supplier modified successfully!");
                System.out.println("The SQL query is: " + strModify);  // Echo For debugging
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error! Invalid data entry");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a name in the textfield");
        }
    }

    public static void assignProj(String projId, String empId, String empName, String department, String role) throws Exception {
        int id;
        int pid;
        try {
            pid = Integer.parseInt(projId);
        } catch (Exception e) {
            throw new Exception("Please enter a numeric id");
        }
        if (empId.equals("") && empName.equals("") && department.equals("") && role.equals("")) {
            throw new Exception("Please select one or more options");
        }
        String search = "select emp_id from employee where 1=1 ";
        if (!empId.equals("")) {
            try {
                id = Integer.parseInt(empId);
            } catch (Exception e) {
                throw new Exception("Please enter a numeric id");
            }
            search += " and emp_id= " + id;
        }
        if (!empName.equals("")) {
            search += "  and emp_name= " + "\"" + empName + "\"";
        }
        if (!department.equals("")) {
            search += " and dept_name= '" + department + "'";
        }
        if (!role.equals("")) {
            search += " and emp_id in(select emp_id from role_emp where role_name= '" + role + "' and active=1)";
        }
        search += ";";

        try {
            rs = st.executeQuery(search);
            int counter=0;
            while (rs.next()) {
                String query = "replace into proj_emp (proj_id, emp_id) values(" + pid + "," + rs.getString("emp_id") + ")";
                st2.executeUpdate(query);
                counter++;
            }
            if(counter>0)
                  JOptionPane.showMessageDialog(null, "Assigned Sucessfully!");
            else
                  JOptionPane.showMessageDialog(null, "No Employees Found!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Assigning Employee!");
        }

    }

    public static void deleteProj(String projId, String empId, String empName, String department, String role) throws Exception {
        int id;
        int pid;
        try {
            pid = Integer.parseInt(projId);
        } catch (Exception e) {
            throw new Exception("Please enter a numeric id");
        }
        if (empId.equals("") && empName.equals("") && department.equals("") && role.equals("")) {
            throw new Exception("Please select one or more options");
        }

        /*   String query = "delete from proj_emp where emp_id in (select emp_id from employee where 1=1";*/
        String with = "delete from proj_emp where proj_id = "+pid+" and emp_id in (select emp_id from employee where 1=1 ";
        if (!empId.equals("")) {
            try {
                id = Integer.parseInt(empId);
            } catch (Exception e) {
                throw new Exception("Please enter a numeric id");
            }
            with += " and emp_id= " + id;
        }
        if (!empName.equals("")) {
            with += " and emp_name= " + "'" + empName + "'";
        }
        if (!department.equals("")) {
            with += " and dept_name= '" + department + "'";
        }

        with += ") ";
        
          if (!role.equals("")) {
            with += " and emp_id in(select emp_id from role_emp where role_name= '" + role + "' and active=1)";
        }

        try {
             int counter = st2.executeUpdate(with);
             if(counter==0)
                 JOptionPane.showMessageDialog(null, "No Employees Found!");
             else
                  JOptionPane.showMessageDialog(null, "Removed Sucessfully!");
        } catch (Exception e) {
               JOptionPane.showMessageDialog(null, "Error Removing Employees!");
        }


      
    }

    public static void projManager(int projId, int empId) {
        String query = "update project set manager = " + empId + " where proj_id= " + projId;
        projectManager r=new projectManager();
        try {
            
           int x= st.executeUpdate(query);
            //System.out.println(x);
            if(x!=0)   
              JOptionPane.showMessageDialog(r, "Manager assigned successfully");
            else
                JOptionPane.showMessageDialog(r, "Please check that both IDs exist");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(r, "Please check that both IDs exist");
        }
    }

    public static void milestone(int id, String date, String des) {
        String query = "insert into milestone values(" + id + ",'" + date + "','" + des + "')";
        try {
            st.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Milestone recorded successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please check the entered data");
        }
    }

    public static void insertRole(String role) {
        String query = "insert into role values('" + role + "')";
        try {
            st.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Role added successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Role already exists!");
        }
    }

    public static ArrayList getRole() {
        String query = "create or replace view roles as select * from role;";
        try {
            st.executeUpdate(query);

        } catch (Exception e) {
            System.out.println(e);
        }

        String strSelect = "select * from roles;";
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();

        try {
            ResultSet rs = st2.executeQuery(strSelect);
            System.out.println("The SQL query is: " + strSelect);
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(rs.getString("role_name"));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);

        }
        return rsString;

    }

    public static ArrayList getDepartments() {
        String strSelect = "select dept_name from department;";
        ArrayList<String> rsString = new ArrayList();

        try {
            ResultSet rs = st2.executeQuery(strSelect);
            System.out.println("The SQL query is: " + strSelect);
            while (rs.next()) {
                rsString.add(rs.getString("dept_name"));
            }

        } catch (SQLException ex) {
            System.out.println(ex);

        }
        return rsString;

    }

    public static JTable requestReport(String sid, String name, String priority, String department,
            String arrived, String supName, int price, String date, String dateinfo, String priceinfo) throws Exception {
        int id = -1;
        if (!sid.equals("")) {
            try {
                id = Integer.parseInt(sid);
            } catch (Exception e) {
                throw new Exception("Please enter numeric ID!");
            }
        }

        String strSelect = "create or replace view request as select * from needs where 1=1 ";
        if (!name.equals("")) {
            strSelect += " and item like '%" + name + "%' ";
        }

        if (id > -1) {
            strSelect += " and request_id = " + id;
        }

        if (!priority.equals("---")) {
            strSelect += " and priority = '" + priority + "' ";
        }
        if (!department.equals("")) {
            strSelect += " and dept_name = '" + department + "' ";
        }
        if (!supName.equals("")) {
            strSelect += " and sup_name = '" + supName + "' ";
        }

        if (arrived.equals("yes")) {
            strSelect += " and arrived = true ";
        }

        if (arrived.equals("no")) {
            strSelect += " and arrived = false ";
        }

        if (price > -1) {
            if (priceinfo.equals("less")) {
                strSelect += " and price <= " + price;
            } else if (priceinfo.equals("more")) {
                strSelect += " and price >= " + price;
            } else {
                strSelect += " and price = " + price;
            }
        }

        if (!date.equals("")) {
            if (dateinfo.equals("before")) {
                strSelect += " and purchase_date <= '" + date + "'";
            } else if (dateinfo.equals("after")) {
                strSelect += " and purchase_date >= '" + date + "'";
            } else {
                strSelect += " and purchase_date = '" + date + "'";
            }
        }
        strSelect += ";";
        System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();

        try {
            st2.executeUpdate(strSelect);
            ResultSet rs = st.executeQuery("select request_id,item,request_date,priority,arrived,dept_name,sup_name,price,purchase_date,"
                    + "(select DATEDIFF(purchase_date,request_date))as delay from request");
            System.out.println("Query entry is successful");
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(Integer.toString(rs.getInt("request_id")));
                temp.add(rs.getString("item"));
                temp.add(rs.getString("request_date"));
                temp.add(rs.getString("priority"));
                temp.add(Boolean.toString(rs.getBoolean("arrived")));
                temp.add(rs.getString("dept_name"));
                temp.add(rs.getString("sup_name"));
                temp.add(Integer.toString(rs.getInt("price")));
                temp.add(rs.getString("purchase_date"));
                temp.add(rs.getString("delay"));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);

        }

        //JTable stats=
        requestStat();
        //  General.viewTable(stats,150,150);
        return General.createCustomTable(rsString, "needs");
    }

    public static void requestStat() {
        String stats = "Select AVG(price) as avg_price,MAX(price) as max_price,MIN(price) as min_price ,MIN(purchase_date) as min_date,Max(purchase_date) as max_date "
                + ", (select item from request where price = (select Max(price) from request) LIMIT 1)as exp "
                + ", (select item from request where price = (select MIN(price) from request)LIMIT 1)as cheap "
                + ", (select item from request where purchase_date = (select MIN(purchase_date) from request)LIMIT 1) as old_item"
                + ", (select item from request where purchase_date =(select MAX(purchase_date)from request) order by request_id desc LIMIT 1) new_item "
                + ", Count(*) as items_count from request ";
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();
        try {
            ResultSet rs = st2.executeQuery(stats);
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(Integer.toString(rs.getInt("avg_price")));
                temp.add(rs.getString("exp"));
                temp.add(Integer.toString(rs.getInt("max_price")));
                temp.add(rs.getString("cheap"));
                temp.add(Integer.toString(rs.getInt("min_price")));
                temp.add(rs.getString("old_item"));
                temp.add(rs.getString("min_date"));
                temp.add(rs.getString("new_item"));
                temp.add(rs.getString("max_date"));
                temp.add(Integer.toString(rs.getInt("items_count")));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);

        } catch (Exception e) {
            System.out.println(e);
        }
        requestStat rstat = new requestStat();
        rstat.set(rsString);
        rstat.setVisible(true);
        //return General.createCustomTable(rsString, "needsReport");
    }

    public static JTable viewMilestone(String sid, String name, String date, String dateinfo) {
        int id = 0;
        if (!sid.equals("")) {
            try {
                id = Integer.parseInt(sid);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please enter numeric ID!");
                return null;
            }
        }

        String query = "Select * from milestone where 1=1 ";
        if (!sid.equals("")) {
            query += " and proj_id= " + id;
        }
        if (!name.equals("")) {
            query += " and proj_id in (select proj_id from project where proj_name like '%" + name + "%')";
        }
        if (!date.equals("")) {
            if (dateinfo.equals("before")) {
                query += " and ms_date <= '" + date + "'";
            }
            if (dateinfo.equals("after")) {
                query += " and ms_date >= '" + date + "'";
            }
            if (dateinfo.equals("")) {
                query += " and ms_date = '" + date + "'";
            }
        }

        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();
        try {
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(Integer.toString(rs.getInt("proj_id")));
                temp.add(rs.getString("ms_date"));
                temp.add(rs.getString("description"));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);

        } catch (Exception e) {
            System.out.println(e);
        }
        return General.createCustomTable(rsString, "milestone");
    }

    public static JTable departmentReport(String name, int budget, String budgetinfo, String headDate, String dateinfo, int numberOfEmp, String numberinfo) {
        String strSelect = "create or replace view departmentreport as select dept_name,dept_budget,head,(select emp_name from employee where emp_id = head) as dept_head,"
                + "head_date,(select count(dept_name) from employee where employee.dept_name = department.dept_name ) AS NOE,"
                +" (select count(dept_name) from proj_dept where proj_dept.dept_name = department.dept_name ) AS NOP"
                +" from department where 1=1 ";
        try {
        st.executeQuery(strSelect);
            
        } catch (Exception e) {
        }
      ///  strSelect="select * from departmentreport where 1=1";
        if (!name.equals("")) {
            strSelect += " and dept_name = '" + name + "'";
        }
        if (budget > 0) {
            if (budgetinfo.equals("less")) {
                strSelect += " and dept_budget <= '" + budget + "'";
            } else if (budgetinfo.equals("more")) {
                strSelect += " and dept_budget >= '" + budget + "'";
            } else {
                strSelect += " and dept_budget = '" + budget + "'";
            }

        }
        if (!headDate.equals("")) {
            if (dateinfo.equals("before")) {
                strSelect += " and head_date <= '" + headDate + "'";
            } else if (dateinfo.equals("after")) {
                strSelect += " and head_date >= '" + headDate + "'";
            } else {
                strSelect += " and head_date = '" + headDate + "'";
            }

        }
        if (numberOfEmp > 0) {
            if (numberinfo.equals("less")) {
                strSelect += " and NOE <= " + numberOfEmp;
            } else if (numberinfo.equals("more")) {
                strSelect += " and NOE >= " + numberOfEmp;
            } else {
                strSelect += " and NOE = " + numberOfEmp;
            }
        }
        strSelect += " group by dept_name";
        
        

        //System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();

        //System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
        try {
            //st2.executeUpdate(strSelect);
            st.executeUpdate(strSelect);
            String strSelect2= "select * from departmentreport ";
            ResultSet rs = st.executeQuery(strSelect2);
            System.out.println("Query entry is successful");
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(rs.getString("dept_name"));
                temp.add(Integer.toString(rs.getInt("dept_budget")));
                temp.add(Integer.toString(rs.getInt("head")));
                temp.add(rs.getString("dept_head"));
                temp.add(rs.getString("head_date"));
                temp.add(Integer.toString(rs.getInt("NOE")));
                temp.add(Integer.toString(rs.getInt("NOP")));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        departmentStats();
        return General.createCustomTable(rsString, "departmentreport");

    }

    public static JTable employeeReport(int id, String employeename, int salary, String salaryinfo,
            String birthDate, String birthinfo, String employmentDate, String employmentinfo,
            String departmentName, String role) {

        String strSelect = "create or replace view employeereport as select employee.emp_id,emp_name,salary,birth_date,address,employment_date,dept_name,role_name,group_concat(distinct(phone)) as phone"
                + " from employee natural join department natural join emp_phone inner join role_emp on employee.emp_id =role_emp.emp_id and active=1    where 1=1 ";

        if (id > 0) {
            strSelect += " and employee.emp_id = " + id;
        }
        if (!employeename.equals("")) {
            strSelect += " and emp_name like '%" + employeename + "%' ";
        }
        if (salary > 0) {
            if (salaryinfo.equals("less")) {
                strSelect += " and salary <= " + salary;
            } else if (salaryinfo.equals("more")) {
                strSelect += " and salary >= " + salary;
            } else {
                strSelect += " and salary = " + salary;
            }
        }
        if (!birthDate.equals("")) {
            Calendar today = Calendar.getInstance();
            int date = today.get(Calendar.YEAR);
            if (birthinfo.equals("before")) {
                strSelect += " and year(birth_date) >= '" + (date-Integer.parseInt(birthDate))+"'";
            } else if (birthinfo.equals("after")) {
                strSelect += " and year(birth_date) <= '" + (date-Integer.parseInt(birthDate))+"'";
            } else {
                strSelect += " and year(birth_date) = '" + (date-Integer.parseInt(birthDate))+"'";
            }
        }
        if (!employmentDate.equals("")) {
            if (employmentinfo.equals("before")) {
                strSelect += " and employment_date <= '" + employmentDate+"'";
            } else if (employmentinfo.equals("after")) {
                strSelect += " and employment_date >= '" + employmentDate+"'";
            } else {
                strSelect += " and employment_date = '" + employmentDate+"'";
            }
        }
        if (!departmentName.equals("")) {
            strSelect += " and dept_name = '" + departmentName + "'";
        }

        if (!role.equals("---")) {
                strSelect += " and  active = 1 and role_name= '" + role+"'";
        }
        strSelect+=" group by employee.emp_id";

        System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();

        try {
            st2.executeUpdate(strSelect);
            ResultSet rs = st.executeQuery("select * from employeereport");
            System.out.println("Query entry is successful");
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(Integer.toString(rs.getInt("emp_id")));
                temp.add(rs.getString("emp_name"));
                temp.add(Integer.toString(rs.getInt("salary")));
                Calendar today = Calendar.getInstance();
                int date = today.get(Calendar.YEAR);
                String age = (date-Integer.parseInt(rs.getString("birth_date").substring(0, 4)))+"";
                temp.add(age);
                temp.add(rs.getString("address"));
                temp.add(rs.getString("employment_date"));
                temp.add(rs.getString("dept_name"));
                temp.add(rs.getString("role_name"));
                temp.add(rs.getString("phone"));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);

        }
        employeeStat();
        return General.createCustomTable(rsString, "employeereport");

    }


    public static JTable projectReport(int id, String name, int budget, String start,
            String due, int client, String department, String budgetinfo, String startinfo, String dueinfo) {

        String strSelect = "create or replace view proj as select proj_id,proj_name,proj_budget,start_date,due_date,client_id,manager"
                + " ,GROUP_CONCAT(DISTINCT(dept_name)) as dept_name  from project natural join proj_dept where 1=1 ";
        if (!name.equals("")) {
            strSelect += " and proj_name like '%" + name + "%' ";
        }

        if (id > -1) {
            strSelect += " and proj_id = " + id;
        }
        if (client > -1) {
            strSelect += " and client_id = " + client;
        }
        if (!department.equals("")) {
            strSelect += " and proj_dept.dept_name= '" + department + "'";
        }
        if (budget > -1) {
            if (budgetinfo.equals("more")) {
                strSelect += " and proj_budget >= " + budget;
            } else if (budgetinfo.equals("less")) {
                strSelect += " and proj_budget <= " + budget;
            } else {
                strSelect += " and proj_budget = " + budget;
            }
        }

        if (!start.equals("")) {
            if (startinfo.equals("before")) {
                strSelect += " and start_date <= '" + start + "'";
            } else if (startinfo.equals("after")) {
                strSelect += " and start_date >= '" + start + "'";
            } else {
                strSelect += " and start_date= '" + start + "'";
            }
        }
        if (!due.equals("")) {
            if (dueinfo.equals("before")) {
                strSelect += " and due_date <= '" + due + "'";
            } else if (dueinfo.equals("after")) {
                strSelect += " and due_date >= '" + due + "'";
            } else {
                strSelect += " and due_date= '" + due + "'";
            }
        }

        strSelect += " GROUP BY proj_id;";
        System.out.println("The SQL query is: " + strSelect);  // Echo For debugging
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();

        try {
            st2.executeUpdate(strSelect);
            ResultSet rs = st.executeQuery("select * from proj");
            System.out.println("Query entry is successful");
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(Integer.toString(rs.getInt("proj_id")));
                temp.add(rs.getString("proj_name"));
                temp.add(Integer.toString(rs.getInt("proj_budget")));
                temp.add(rs.getString("start_date"));
                temp.add(rs.getString("due_date"));
                temp.add(Integer.toString(rs.getInt("client_id")));
                temp.add(Integer.toString(rs.getInt("manager")));
                temp.add(rs.getString("dept_name"));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);

        }
        projectStat();
        return General.createCustomTable(rsString, "project");
    }

    public static void projectStat() {
        String stats = "Select MAX(proj_budget) as max_budget,MIN(proj_budget) as min_budget ,MIN(start_date) as min_sdate, Max(start_date) as max_sdate "
                + ", MIN(due_date) as min_ddate, MAX(due_date) as max_ddate"
                + ", (select proj_name from proj where proj_budget = (select Max(proj_budget) from proj) LIMIT 1)as highest "
                + ", (select proj_name from proj where proj_budget = (select MIN(proj_budget) from proj)LIMIT 1)as lowest "
                + ", (select proj_name from proj where start_date = (select MIN(start_date) from proj)LIMIT 1) as earliest"
                + ", (select proj_name from proj where start_date =(select MAX(start_date)from proj) order by proj_id desc LIMIT 1) latest "
                + ", (select proj_name from proj where due_date = (select MIN(due_date) from proj)LIMIT 1) as nearest"
                + ", (select proj_name from proj where due_date =(select MAX(due_date)from proj) order by proj_id desc LIMIT 1) furthest "
                + ", (Count(distinct(proj_id))) as projects_count from proj ";
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();
        try {
            ResultSet rs = st2.executeQuery(stats);
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(rs.getString("highest"));
                temp.add(Integer.toString(rs.getInt("max_budget")));
                temp.add(rs.getString("lowest"));
                temp.add(Integer.toString(rs.getInt("min_budget")));
                temp.add(rs.getString("earliest"));
                temp.add(rs.getString("latest"));
                temp.add(rs.getString("furthest"));
                temp.add(rs.getString("nearest"));
                temp.add(rs.getString("min_sdate"));
                temp.add(rs.getString("max_sdate"));
                temp.add(rs.getString("min_ddate"));
                temp.add(rs.getString("max_ddate"));
                temp.add(Integer.toString(rs.getInt("projects_count")));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);

        } catch (Exception e) {
            System.out.println(e);
        }
        projectStat pstat = new projectStat();
        pstat.set(rsString);
        pstat.setVisible(true);
    }

    public static JTable viewProj(int id) {

        String stats = "Select emp_id,emp_name, role_emp.role_name,salary from employee natural join role_emp where role_emp.active=1 and "
                + "emp_id in (select emp_id from proj_emp where proj_id= " + id + ")";

        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();
        try {
            ResultSet rs = st2.executeQuery(stats);
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(Integer.toString(rs.getInt("emp_id")));
                temp.add(rs.getString("emp_name"));
                temp.add(rs.getString("role_name"));
                temp.add(Integer.toString(rs.getInt("salary")));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);

        } catch (Exception e) {
            System.out.println(e);
        }
  
        return General.createCustomTable(rsString, "proj_emp");
    }

    public static void employeeStat(){
        String stats = "Select MAX(salary) as max_salary,MIN(salary) as min_salary ,MIN(birth_date) as min_bdate, Max(birth_date) as max_bdate "
                + ", MIN(employment_date) as min_edate, MAX(employment_date) as max_edate"
                + ", (select emp_name from employeereport where salary = (select Max(salary) from employeereport) LIMIT 1)as highest "
                + ", (select emp_name from employeereport where salary = (select MIN(salary) from employeereport)LIMIT 1)as lowest "
                + ", (select emp_name from employeereport where birth_date = (select MIN(birth_date) from employeereport)LIMIT 1) as eldest"
                + ", (select emp_name from employeereport where birth_date =(select MAX(birth_date)from employeereport) order by emp_id desc LIMIT 1) youngest "
                + ", (select emp_name from employeereport where employment_date = (select MIN(employment_date) from employeereport)LIMIT 1) as oldest"
                + ", (select emp_name from employeereport where employment_date =(select MAX(employment_date)from employeereport) order by emp_id desc LIMIT 1) newest "
                + ", (Count(distinct(emp_id))) as employees_count from employeereport ";
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();
        try {
            ResultSet rs = st2.executeQuery(stats);
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(rs.getString("highest"));
                temp.add(Integer.toString(rs.getInt("max_salary")));
                temp.add(rs.getString("lowest"));
                temp.add(Integer.toString(rs.getInt("min_salary")));
                temp.add(rs.getString("oldest"));
                temp.add(rs.getString("newest"));
                temp.add(rs.getString("youngest"));
                temp.add(rs.getString("eldest"));
                temp.add(rs.getString("min_bdate"));
                temp.add(rs.getString("max_bdate"));
                temp.add(rs.getString("min_edate"));
                temp.add(rs.getString("max_edate"));
                temp.add(Integer.toString(rs.getInt("employees_count")));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);

        } catch (Exception e) {
            System.out.println(e);
        }
        employeeStat estat = new employeeStat();
        estat.set(rsString);
        estat.setVisible(true);
    }


    public static void departmentStats(){
         String stats = "Select MAX(dept_budget) as max_budget,MIN(dept_budget) as min_budget"
                + ", AVG(dept_budget) as avg_budget"
                + ", (select dept_name from departmentreport where dept_budget = (select Max(dept_budget) from departmentreport) LIMIT 1)as highest "
                + ", (select dept_name from departmentreport where dept_budget = (select MIN(dept_budget) from departmentreport)LIMIT 1)as lowest "
                + ", (select dept_name from departmentreport where NOE = (select MAX(NOE) from departmentreport)LIMIT 1) as mostemp"
                + ", (select dept_name from departmentreport where NOP =(select MAX(NOP)from departmentreport)  LIMIT 1) as mostproj"
                 +" from departmentreport";
        ArrayList<ArrayList<String>> rsString = new ArrayList<ArrayList<String>>();
        try {
            ResultSet rs = st2.executeQuery(stats);
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(rs.getString("highest"));
                temp.add(Integer.toString(rs.getInt("max_budget")));
                temp.add(rs.getString("lowest"));
                temp.add(Integer.toString(rs.getInt("min_budget")));
                temp.add(rs.getString("mostemp"));
                temp.add(rs.getString("mostproj"));
                temp.add(Integer.toString(rs.getInt("avg_budget")));
                rsString.add(temp);
            }
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < rsString.get(i).size(); j++) {
                    if (rsString.get(i).get(j) != null && rsString.get(i).get(j).equals("0")) {
                        rsString.get(i).set(j, "");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);

        } catch (Exception e) {
            System.out.println(e);
        }
        departmentStat dstat = new departmentStat();
        dstat.set(rsString);
        dstat.setVisible(true);
    }

    
    
    public static boolean compareDate(String small, String big){

        if(Integer.parseInt(small.substring(0,4))>Integer.parseInt(big.substring(0,4))){
            return false;
        }
        if(Integer.parseInt(small.substring(0,4))==Integer.parseInt(big.substring(0,4))){
            if(Integer.parseInt(small.substring(5,7))>Integer.parseInt(big.substring(5,7)))
                return false;
            if(Integer.parseInt(small.substring(5,7))==Integer.parseInt(big.substring(5,7))){
                if(Integer.parseInt(small.substring(8,10))>Integer.parseInt(big.substring(8,10)))
                     return false;
            }
                
        }
        return true;
        
    }
}
