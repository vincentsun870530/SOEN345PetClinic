package org.springframework.samples.petclinic.consistencychecker;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;

import java.util.List;

public class OwnerConsistencyChecker implements InConsistencyChecker {

    private static List<Owner> oldOwnersData;
    private static List<Owner> newOwnersData;
    
    public static void setOldData(List<Owner> oldTableData) {
        // this.oldOwnersData = oldTableData;
        oldOwnersData = oldTableData;
    }

    public static void setNewData(List<Owner> newTableData) {
        // this.newOwnersData = newTableData;
        newOwnersData = newTableData;
    }

    public int consistencyChecker() {
        Owner oldOwner;
        Owner newOwner;
        int atID;
        int inconsistency = 0;
        for(int index=0; index < oldOwnersData.size(); index++) {
            oldOwner = oldOwnersData.get(index);
            newOwner = newOwnersData.get(index);
            atID = newOwner.getId();
            if (inconsistency!= 0) {
                System.out.println("OLD:" + oldOwnersData.get(index));
                System.out.println("NEW:" + newOwnersData.get(index));
            }
            //need the number of columns (use hardcoded number or dynamically check the number of columns)
            //for Owner, 5 columns
            if (oldOwner.getId() != newOwner.getId()) {
                System.out.println("\t" + oldOwner.getId() + "!=" + newOwner.getId());
                inconsistency++;
            } else if (!oldOwner.getFirstName().equals(newOwner.getFirstName())) {
                checkNewAndOldData(atID, oldOwner.getFirstName(), newOwner.getFirstName(), "first_name");
                inconsistency++;
            } else if (!oldOwner.getLastName().equals(newOwner.getLastName())) {
                checkNewAndOldData(atID, oldOwner.getLastName(), newOwner.getLastName(), "last_name");
                inconsistency++;
            } else if (!oldOwner.getAddress().equals(newOwner.getAddress())) {
                checkNewAndOldData(atID, oldOwner.getAddress(), newOwner.getAddress(), "address");
                inconsistency++;
            } else if (!oldOwner.getCity().equals(newOwner.getCity())) {
                checkNewAndOldData(atID, oldOwner.getCity(), newOwner.getCity(), "city");
                inconsistency++;
            } else if (!oldOwner.getTelephone().equals(newOwner.getTelephone())) {
                checkNewAndOldData(atID, oldOwner.getTelephone(), newOwner.getTelephone(), "telephone");
                inconsistency++;
            }
        }
        return inconsistency;
    }

    public double calculateConsistencyChecker(int inconsistency) {
        int sizeOfOwners = oldOwnersData.size();
        double consistency = (1 - (inconsistency/sizeOfOwners))*100;
        return Double.parseDouble(String.format("%.2f", consistency));
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName){
        checkNewAndOldData(id,oldData,newData,columnName,"owners");
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName, String tableName) {
        if(!(oldData.equals(newData))) {
            printViolationMessage(id, oldData, newData);
            SQLiteDBConnector.getInstance().updateById(tableName,columnName, oldData, id);
        }
    }

    public void printViolationMessage(int id, String oldData, String newData) {
        System.out.println("The row " + id + " on the new database," +
                            " does not match: New(" + newData + 
                            " is not equal to Old(" + oldData);
    }

    // public static void main(String[] args) {
    //     ResultSet rsNew = new SQLiteDBConnector().selectAll("owners");
    //     List<Owner> ownersListNew = new ArrayList<Owner>();
    //     Owner ownerNew;
    //     try {
    //         while (rsNew.next()) {
    //             int id = rsNew.getInt("id");
    //             String firstName = rsNew.getString("first_name");
    //             String lastName = rsNew.getString("last_name");
    //             String address = rsNew.getString("address");
    //             String city = rsNew.getString("city");
    //             String telephone = rsNew.getString("telephone");

    //             ownerNew = new Owner();
    //             ownerNew.setId(id);
    //             ownerNew.setFirstName(firstName);
    //             ownerNew.setLastName(lastName);
    //             ownerNew.setAddress(address);
    //             ownerNew.setCity(city);
    //             ownerNew.setTelephone(telephone);

    //             ownersListNew.add(ownerNew);
    //         }
    //         setNewData(ownersListNew);
    //     } catch (SQLException exception) {

    //     }

    //     ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("owners");
    //     List<Owner> ownersListOld = new ArrayList<Owner>();
    //     Owner ownerOld;
    //     try {
    //         while (rsOld.next()) {
    //             int id = rsOld.getInt("id");
    //             String firstName = rsOld.getString("first_name");
    //             String lastName = rsOld.getString("last_name");
    //             String address = rsOld.getString("address");
    //             String city = rsOld.getString("city");
    //             String telephone = rsOld.getString("telephone");

    //             ownerOld = new Owner();
    //             ownerOld.setFirstName(firstName);
    //             ownerOld.setLastName(lastName);
    //             ownerOld.setAddress(address);
    //             ownerOld.setCity(city);
    //             ownerOld.setTelephone(telephone);

    //             ownersListOld.add(ownerOld);
    //         }
    //         setOldData(ownersListOld);
    //     } catch (SQLException exception) {

    //     }

    //     new OwnerConsistencyChecker().consistencyChecker();

    // }

}