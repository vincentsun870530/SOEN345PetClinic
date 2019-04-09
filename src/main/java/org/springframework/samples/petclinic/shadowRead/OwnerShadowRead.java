package org.springframework.samples.petclinic.shadowRead;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.sqlite.SQLiteOwnerHelper;
import org.springframework.scheduling.annotation.Async;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OwnerShadowRead {
		
		private int readInconsistencies = 0;
		private int inconsistencies = 0;
		SQLiteDBConnector sqLiteDbConnector = SQLiteDBConnector.getInstance();
		SQLiteOwnerHelper sqLiteOwnerHelper = SQLiteOwnerHelper.getInstance();
		UpdateOwner updateOwner = new UpdateOwner();
        private static Logger log = LogManager.getLogger(OwnerShadowRead.class);

		@Async
		public int checkOwner(Owner owner){
			int inconsistencyId = -1;
			//Owner in SqLite db with the specified id
			ResultSet rs = sqLiteDbConnector.selectById("owners", owner.getId());
			//initialise variables
			String firstName = null;
			String lastName = null;
			String address = null;
			String city = null;
			String telephone = null;

			try {
				//get the number of the column in the SqLite database
				int first_name_col = rs.findColumn("first_name");
				int last_name_col = rs.findColumn("last_name");
				int address_col = rs.findColumn("address");
				int city_col = rs.findColumn("city");
				int telephone_col = rs.findColumn("telephone");

				//Obtain the value of the row proper to the specified id nin the SqLite db
				if (rs != null) {
					firstName = (String) rs.getObject(first_name_col);
					lastName = (String) rs.getObject(last_name_col);
					address = (String) rs.getObject(address_col);
					city = (String) rs.getObject(city_col);
					telephone = (String) rs.getObject(telephone_col);
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();}

			//checking that the values of the rows retrieved from both old and new databases are the same
			//when the rows are different increment readConsistencies and print the inconsistency
			if(!(owner.getFirstName().equals(firstName))){
				ReadInconsistency(owner.getFirstName(), firstName);
			//	updateOwner.updateFirstName(owner);
				inconsistencyId ++;
			}

			log.debug(owner.getFirstName() + " | " + firstName );

			if(!(owner.getLastName().equals(lastName))){
				ReadInconsistency(owner.getLastName(), lastName);
			//	updateOwner.updateLastName(owner);
				inconsistencyId ++;
			}

            log.debug(owner.getLastName() + " | " + lastName );

			if(!(owner.getAddress().equals(address))){
				ReadInconsistency(owner.getAddress(), address);
			//	updateOwner.updateAddress(owner);
				inconsistencyId ++;
			}

            log.debug(owner.getAddress() + " | " + address );

			if(!(owner.getCity().equals(city))){
				ReadInconsistency(owner.getCity(), city);
			//	updateOwner.updateCity(owner);
				inconsistencyId ++;
			}

            log.debug(owner.getCity() + " | " + city );

			if(!(owner.getTelephone().equals(telephone))){
				ReadInconsistency(owner.getTelephone(), telephone);
			//	updateOwner.updateTelephone(owner);
				inconsistencyId ++;
			}

            log.debug(owner.getTelephone() + " | " + telephone );

			return inconsistencyId;
		}

		//print read inconsistency
		private void ReadInconsistency(Object oldRow, Object newRow) {
			log.error("\n Old object in Mysql db does not match the new object in SqLite db" + "\n Old: " + oldRow	+ "\n New: " + newRow);
			readInconsistencies++;
            log.error("\n Read inconsistencies = " + readInconsistencies);
		}
		
		public int getReadInconsistencies() {
			return readInconsistencies;
		}


	}

