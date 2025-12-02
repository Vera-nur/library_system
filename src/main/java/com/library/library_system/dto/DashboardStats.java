package com.library.library_system.dto;

public interface DashboardStats {
    Integer getTotalBooks();
    Integer getActiveLoans();
    Integer getOverdueBooks();
    Integer getTotalMembers();
}