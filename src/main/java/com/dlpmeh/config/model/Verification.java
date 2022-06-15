package com.dlpmeh.config.model;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Stores a user’s verification state, validity period, and plan type.
 */
@Data
public class Verification {
	private boolean status = false;
	private LocalDateTime startedAt;
	private LocalDateTime endsAt;
	private String planType;

}
