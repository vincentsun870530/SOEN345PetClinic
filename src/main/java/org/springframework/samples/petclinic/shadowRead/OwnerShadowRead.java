package org.springframework.samples.petclinic.shadowRead;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.sqlite.SQLiteOwnerHelper;
import org.springframework.scheduling.annotation.Async;

public class OwnerShadowRead {
		
		private int readInconsistencies = 0;
		private int inconsistencies = 0;
		SQLiteDBConnector sqLiteDbConnector = SQLiteDBConnector.getInstance();
		SQLiteOwnerHelper sqLiteOwnerHelper = SQLiteOwnerHelper.getInstance();
		UpdateOwner updateOwner = new UpdateOwner();
		
		@Async
		public int checkOwner(Owner owner) throws SQLException{
			int inconsistencyId = -1;
			//Owner in SqLite db with the specified id
			ResultSet result = sqLiteDbConnector.selectById("owners", owner.getId());
			//initialise variables
			String firstName = null;
			String lastName = null;
			String address = null;
			String city = null;
			String telephone = null;
			
			//get the number of the column in the SqLite database
			int first_name_col = result.findColumn("first_name");
			int last_name_col = result.findColumn("last_name");
			int address_col = result.findColumn("address");
			int city_col = result.findColumn("city");
			int telephone_col = result.findColumn("telephone");
			
			//Obtain the value of the row proper to the specified id nin the SqLite db
			if(result != null) {
				firstName = (String) result.getObject(first_name_col);
				lastName = (String) result.getObject(last_name_col);
				address = (String) result.getObject(address_col);
				city = (String) result.getObject(city_col);
				telephone = (String) result.getObject(telephone_col);
			}

			//checking that the values of the rows retrieved from both old and new databases are the same
			//when the rows are different increment readConsistencies and print the inconsistency
			if(!(owner.getFirstName().equals(firstName))){
				ReadInconsistency(owner.getFirstName(), firstName);
				updateOwner.updateFirstName(owner);
			}

			if(!(owner.getLastName().equals(lastName))){
				ReadInconsistency(owner.getLastName(), lastName);
			//	updateOwner.updateLastName(owner);
			}

			if(!(owner.getAddress().equals(address))){
				ReadInconsistency(owner.getAddress(), address);
			//	updateOwner.updateAddress(owner);
			}

			if(!(owner.getCity().equals(city))){
				ReadInconsistency(owner.getCity(), city);
			//	updateOwner.updateCity(owner);
			}

			if(!(owner.getTelephone().equals(telephone))){
				ReadInconsistency(owner.getTelephone(), telephone);
			//	updateOwner.updateTelephone(owner);
			}

			return inconsistencyId;
		}

		//print read inconsistency
		private void ReadInconsistency(Object oldRow, Object newRow) {
			System.out.println("\n Old object in Mysql db does not match the new object in SqLite db" + "\n Old: " + oldRow	+ "\n New: " + newRow);
			readInconsistencies++;
			System.out.println("\n Read inconsistencies = " + readInconsistencies);
		}
		
		public int getReadInconsistencies() {
			return readInconsistencies;
		}
		public int getInconsistencies() {
			
			return inconsistencies;
		}

	}

