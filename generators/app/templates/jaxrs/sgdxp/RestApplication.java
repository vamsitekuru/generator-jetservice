package org.jetservice;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.xtivia.sgdxp.core.SgDxpApplication;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;

/*
 * A sample application to demonstrate implementing a JAX-RS endpoint in DXP
 */
@Component(immediate=true, 
           service=Application.class,
           configurationPid = "<%=serviceName%>",
           configurationPolicy = ConfigurationPolicy.OPTIONAL,
           property={"jaxrs.application=true"} 
)
@ApplicationPath("<%=servicePath%>")
public class RestApplication extends SgDxpApplication {

    public RestApplication() {
        super();

        //add the automated Jackson marshaller for JSON
        this.singletons.add(new JacksonJsonProvider());

        // add our REST endpoints (resources)
        this.singletons.add(new PeopleResource());
    }
	
	/*
	 * Register our JAX-RS providers and resources
	 */
	@Override
	public Set<Object> getSingletons() {

	    Set<Object> _singletons = new HashSet<>();
	    _singletons.addAll(super.getSingletons());
	    _singletons.addAll(this.singletons);
		return _singletons;
	}
	
	/*
	 * This method demonstrates how you can perform logic when your bundle is activated/updated. For simplicity's
	 * sake we print a message to the console--this is particularly useful during update-style deployments. 
	 * 
	 * This method will also be invoked when the OSGi configuration changes for this bundle and we reflect that
	 * by printing out the current value of our configuration object.
	 */
	@Activate
	@Modified
	public void activate(Map<String, Object> properties) {
	
		System.out.println("The sample DXP REST app has been activated/updated at " + new Date().toString());
		
		/*
		 * Demonstrate updates to the configuration object for this bundle. 
		 */
	
		this._restConfiguration = ConfigurableUtil.createConfigurable(RestApplicationConfiguration.class, properties);
		
		if (this._restConfiguration != null) {
			System.out.println("For sample DXP REST config, info=" + this._restConfiguration.info());
			System.out.println("For sample DXP REST config, infoNum=" + this._restConfiguration.infoNum());
		} else {
			System.out.println("The sample DXP REST config object is not yet initialized");
		}
	}
	
	private RestApplicationConfiguration _restConfiguration;
    private Set<Object> singletons = new HashSet<Object>();
}