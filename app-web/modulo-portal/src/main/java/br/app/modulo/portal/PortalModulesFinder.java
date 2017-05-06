package br.app.modulo.portal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.extensions.model.fluidgrid.FluidGridItem;

import br.app.modulo.infra.api.ModuleDescription;
import br.app.modulo.infra.api.ModulesFinder;
import br.app.modulo.infra.bean.MessagesProxy;
import br.app.modulo.infra.comparator.ModuleDescriptionComparator;

/**
 * Collects all available JSF modules.
 */
@ApplicationScoped
@Named
public class PortalModulesFinder implements ModulesFinder
{
	@Any
	@Inject
	private Instance<ModuleDescription> moduleDescriptions;

	@Inject
	private MessagesProxy msgs;

	private List<FluidGridItem> modules;

	@Override
	public List<FluidGridItem> getModules()
	{
		if (modules != null) {
			return modules;
		}

		modules = new ArrayList<FluidGridItem>();

		for (ModuleDescription moduleDescription : moduleDescriptions) {
			modules.add(new FluidGridItem(moduleDescription));
		}

		// sort modules by names alphabetically
		Collections.sort(modules, ModuleDescriptionComparator.getInstance());

		return modules;
	}
}
