/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package company.data.management.system;

import javax.swing.*;
import java.util.ArrayList;

public class General {

    //table = Department -> Department name, Department Budget, Head Employee ID, Head Time
    public static JTable createCustomTable(ArrayList<ArrayList<String>> rsString, String relation) {
        if (relation.equals("department")) {
            String data[][] = new String[rsString.size()][4];
            String column[] = {"Department name", "Department Budget", "Head Employee ID", "Head time"};
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < 4; j++) {
                    data[i][j] = rsString.get(i).get(j);
                }
            }
            JTable output = new JTable(data, column);
            return output;
        } else if (relation.equals("employee")) {
            String data[][] = new String[rsString.size()][11];
            String column[] = {"Employee ID", "Employee name", "Salary", "Age", "Employee mail", "Employment date", "Department name", "Address", "Role Name", "Role Date", "Phone Number"};
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < 11; j++) {
                    data[i][j] = rsString.get(i).get(j);

                }
            }
            JTable output = new JTable(data, column);
            return output;
        } else if (relation.equals("needs")) {
            String data[][] = new String[rsString.size()][10];
            String column[] = {"Request ID", "Item name", "Request Date", "Priority", "Arrived", "Department", "Supplier", "Price", "Purchase Date", "Delay (Days)"};
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < 10; j++) {
                    data[i][j] = rsString.get(i).get(j);
                }
            }
            JTable output = new JTable(data, column);
            return output;
        } else if (relation.equals("project")) {
            String data[][] = new String[rsString.size()][8];
            String column[] = {"Project ID", "Project Name", "Project Budget", "Start Date", "Due Date", "Client ID","Manager ID","Department"};
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < 8; j++) {
                    data[i][j] = rsString.get(i).get(j);
                }
            }
            JTable output = new JTable(data, column);
            return output;
        }
        else if (relation.equals("project2")) {
            String data[][] = new String[rsString.size()][7];
            String column[] = {"Project ID", "Project Name", "Project Budget", "Start Date", "Due Date", "Client ID","Manager ID","Department"};
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < 7; j++) {
                    data[i][j] = rsString.get(i).get(j);
                }
            }
            JTable output = new JTable(data, column);
            return output;
        }
        else if (relation.equals("client")) {
            String data[][] = new String[rsString.size()][5];
            String column[] = {"Client ID", "Client Name", "Client Mail", "Client Phone", "Client City"};
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < 5; j++) {
                    data[i][j] = rsString.get(i).get(j);
                }
            }
            JTable output = new JTable(data, column);
            return output;
        } else if (relation.equals("supplier")) {
            String data[][] = new String[rsString.size()][4];
            String column[] = {"Supplier Name", "Supplier City", "Supplier Mail", "Supplier Phone"};
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < 4; j++) {
                    data[i][j] = rsString.get(i).get(j);
                }
            }
            JTable output = new JTable(data, column);
            return output;
        } /*else if (relation.equals("roles"))
        {
            String data[][]=new String[rsString.size()][1];
            String column[]={"Role Name"};
            for(int i=0;i<rsString.size();i++)
            {
                for(int j=0;j<1;j++)
                {
                    data[i][j]=rsString.get(i).get(j);
                }
            }
            JTable output=new JTable(data,column);
            return output;
        }   else if (relation.equals("needsReport"))
        {
            String data[][]=new String[rsString.size()][9];
            String column[]={"Average Price","Most Expensive Item","Its Price","Cheapest Item","Its Price","Oldest Purchased Item",
                "Oldest Purchase Date","Newest Purchased Item","Newest Purchase Date"};
            for(int i=0;i<rsString.size();i++)
            {
                for(int j=0;j<9;j++)
                {
                    data[i][j]=rsString.get(i).get(j);
                }
            }
            JTable output=new JTable(data,column);
            return output;
        }*/
        else if (relation.equals("milestone"))
        {
            String data[][]=new String[rsString.size()][3];
            String column[]={"Project ID","Milestone Date","Description"};
            for(int i=0;i<rsString.size();i++)
            {
                for(int j=0;j<3;j++)
                {
                    data[i][j]=rsString.get(i).get(j);
                }
            }
            JTable output=new JTable(data,column);
            return output;
            
            
        } 
        else if (relation.equals("employeereport")) {
            String data[][] = new String[rsString.size()][9];
            String column[] = {"Employee ID", "Employee name" ,"Salary", "Age","Address", "Employment date", "Department name","Role","Phone Number"};
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < 9; j++) {
                    data[i][j] = rsString.get(i).get(j);
                }
            }
            JTable output = new JTable(data, column);
            return output;
        }



        else if (relation.equals("departmentreport")) {
            String data[][] = new String[rsString.size()][7];
            String column[] = {"Department Name", "Department Budget" ,"Head ID","Head Name","Head Date", "Number of Employess","Number of Projects"};
            for (int i = 0; i < rsString.size(); i++) {
                for (int j = 0; j < 7; j++) {
                    data[i][j] = rsString.get(i).get(j);
                }
            }
            JTable output = new JTable(data, column);
            return output;
        }
        else if (relation.equals("proj_emp"))
        {
            String data[][]=new String[rsString.size()][4];
            String column[]={"Employee ID","Employee Name","Role","Salary"};
            for(int i=0;i<rsString.size();i++)
            {
                for(int j=0;j<4;j++)
                {
                    data[i][j]=rsString.get(i).get(j);
                }
            }
            JTable output=new JTable(data,column);
            return output;
        }
        else {
            return null;
        }
        

    }

    public static void viewTable(JTable mainTable, int x, int y) {

        JFrame f;
        f = new JFrame();
        f.setLocation(x, y);
        //Unsure wheither or not I need this line
        //mainTable.setBounds(30, 40, 200, 300);
        JScrollPane sp = new JScrollPane(mainTable);
        f.add(sp);
        f.setBounds(400,250,1100,400);
        f.setVisible(true);

    }
}
