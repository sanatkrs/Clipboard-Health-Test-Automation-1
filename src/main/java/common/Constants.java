package common;

import config.TestConfig;
import config.TestConfigConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Objective:
 * House String Constants here that may be referenced heavily within the Stack
 * Example: API routes, Tenant URLs across the pipeline, entity nomenclature, etc.
 */
@Slf4j
public class Constants extends SystemContextConstants {

	// Disable instantiation :)
	private Constants() {}

}
