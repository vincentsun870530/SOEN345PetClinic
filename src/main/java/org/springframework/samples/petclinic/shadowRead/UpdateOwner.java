package org.springframework.samples.petclinic.shadowRead;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;

public class UpdateOwner {
	
	SQLiteDBConnector sqLiteDbConnector = SQLiteDBConnector.getInstance();
	
    @SuppressWarnings("unused")
	void updateFirstName(Owner owner){
		sqLiteDbConnector. updateById("owners", "first_name", owner.getFirstName(), owner.getId());
	}
	
	//update last name in SqLite db
	@SuppressWarnings("unused")
	public void updateLastName(Owner owner){
		sqLiteDbConnector. updateById("owners", "last_name", owner.getLastName(), owner.getId());
	}
	
	//update address in SqLite db
	@SuppressWarnings("unused")
	public void updateAddress(Owner owner){
		sqLiteDbConnector. updateById("owners", "address", owner.getAddress(), owner.getId());
	}
	
	//update city in SqLite db
	@SuppressWarnings("unused")
	public void updateCity(Owner owner){
		sqLiteDbConnector. updateById("owners", "city", owner.getCity(), owner.getId());
	}
	
	//update telephone in SqLite db
	@SuppressWarnings("unused")
	public void updateTelephone(Owner owner){
		sqLiteDbConnector. updateById("owners", "telephone", owner.getTelephone(), owner.getId());
	}
	
}
