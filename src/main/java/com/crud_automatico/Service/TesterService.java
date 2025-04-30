package com.crud_automatico.Service;
import com.crud_automatico.Persistence.Entity.TesterEntity;
import com.crud_automatico.Persistence.Repository.TesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;import java.util.List;


@Service
public class TesterService {

	private final TesterRepository testerrepository;

	@Autowired
	public TesterService(TesterRepository testerrepository) {
		    this.testerrepository = testerrepository;
	}

	public List<TesterEntity> getAll(){
		return testerrepository.findAll();
	}

	public TesterEntity save(TesterEntity tester){
		try{
			return testerrepository.save(tester);
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	public TesterEntity update(TesterEntity tester){
		return testerrepository.save(tester);
	}

	public boolean delete(int id){
		try{
			if(!testerrepository.existsById(id)){
				return false;
			}
			testerrepository.deleteById(id);
			return true;
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}