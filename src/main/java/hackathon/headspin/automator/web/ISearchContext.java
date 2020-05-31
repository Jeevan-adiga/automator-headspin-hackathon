package hackathon.headspin.automator.web;

import org.openqa.selenium.By;

public interface ISearchContext {
	
	public ISearchContext getParent();
	public Navigator findElement(By by);
	public ISearchContext findElement(String name, By by);

}
