package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListModelDTO <E extends ModelDTO> extends ModelDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<E> list;

    public ListModelDTO() {
        this.list = new ArrayList<>();
    }
    

    public ListModelDTO(List<E> list) {
		super();
		this.list = list;
	}
	public void add(E element) {
        list.add(element);
    }

    public E get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }
    
    public List<E> getList(){
    	return this.list;
    }


	
	

}
