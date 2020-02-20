package fr.noobeclair.hashcode.bean.hashcode2020;

import fr.noobeclair.hashcode.bean.BeanContainer;

public class HashCode2020BeanContainer extends BeanContainer {
    
    private In in;
    
    private Temp temp;
    
    private Out out;

	public HashCode2020BeanContainer(String inName) {
		super(inName);
	}

	@Override
	public Object getNew() {
		// Add custom properties copy
		// this.mycustom properties = o.custom;
		return this;
	}

    public In getIn() {
        return in;
    }

    public void setIn(In in) {
        this.in = in;
    }

    
    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public Out getOut() {
        return out;
    }

    public void setOut(Out out) {
        this.out = out;
    }
	
	

}
