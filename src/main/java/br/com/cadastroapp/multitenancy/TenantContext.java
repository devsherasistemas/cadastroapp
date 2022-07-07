package br.com.cadastroapp.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Alon Segal on 16/03/2017.
 */
public class TenantContext {

    private static final Logger logger = LoggerFactory.getLogger(TenantContext.class.getName());

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(String tenant) {
        logger.debug("Setting tenant to " + tenant);
        currentTenant.set(tenant);
        System.err.println("O tenant é " + tenant);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.set(null);
    }
}
