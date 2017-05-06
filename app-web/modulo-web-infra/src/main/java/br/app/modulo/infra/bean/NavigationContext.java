package br.app.modulo.infra.bean;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;

import br.app.modulo.infra.api.ModuleDescription;
import br.app.modulo.infra.api.ModulesFinder;
import br.app.modulo.infra.util.FacesUtils;

import java.io.Serializable;

/**
 * Navigation context infos.
 */
@Named
@SessionScoped
public class NavigationContext implements Serializable {
	private static final String PORTAL_HOME = "/views/portalHome.xhtml";

	// ~ Instance fields
	// --------------------------------------------------------

	/* boolean flag if the particular web application is embedded in a portal */
	private Boolean withinPortal;

	/** path to master layout template */
	private String masterLayoutTemplate;

	/** current prefix when the web app is running within "portal" */
	private String prefix;

	public String getMenuitemStyleClass(String page) {
		String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		if (page.equals(viewId)) {
			return "ui-state-active";
		}

		return "";
	}

	public boolean isPortalHome() {
		return PORTAL_HOME.equals(FacesContext.getCurrentInstance().getViewRoot().getViewId());
	}

	public boolean isWithinPortal() {
		if (withinPortal == null) {
			withinPortal = FacesUtils.isBeanExist(ModulesFinder.class);
		}

		return withinPortal;
	}

	public String getMasterLayoutTemplate() {
		if (masterLayoutTemplate == null) {
			if (prefix == null && !isWithinPortal()) {
				// not in portal
				ModuleDescription modDesc = FacesUtils.lookup(ModuleDescription.class);
				prefix = modDesc.getPrefix();
			}

			if (prefix != null) {
				masterLayoutTemplate = "/" + prefix + "/templates/masterLayout.xhtml";
			} else {
				masterLayoutTemplate = "/templates/masterLayout.xhtml";
			}
		}

		return masterLayoutTemplate;
	}

	public void goToPortlet(ModuleDescription modDesc) {
		try {
			masterLayoutTemplate = null; // force reload
			prefix = modDesc.getPrefix();

			if (modDesc.getUrl().contains(".xhtml") || modDesc.getUrl().contains(".jsf")) {
				FacesContext fc = FacesContext.getCurrentInstance();
				fc.getApplication().getNavigationHandler().handleNavigation(fc, "null",
						modDesc.getUrl() + "?faces-redirect=true");
			}
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ex = fc.getExternalContext();
			String path = ex.getRequestContextPath();
			ex.redirect(path.concat(modDesc.getUrl()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
