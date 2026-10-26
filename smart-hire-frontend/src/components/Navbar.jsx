import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useTheme } from '../context/ThemeContext';
import { Briefcase, User, LogOut, ShieldCheck, ChevronDown, Building2, Sun, Moon } from 'lucide-react';
import NotificationBell from './NotificationBell';

const Navbar = () => {
  const { user, logout } = useAuth();
  const { theme, toggleTheme } = useTheme();
  const navigate = useNavigate();
  const [dropdownOpen, setDropdownOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav style={{
      background: theme === 'dark' ? 'rgba(17, 24, 39, 0.9)' : 'rgba(255, 255, 255, 0.92)',
      backdropFilter: 'blur(12px)',
      borderBottom: '1px solid var(--border-color)',
      position: 'sticky',
      top: 0,
      zIndex: 100,
      transition: 'all 0.25s ease'
    }}>
      <div style={{
        maxWidth: '1280px',
        margin: '0 auto',
        padding: '0.85rem 1.5rem',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        flexWrap: 'wrap',
        gap: '1rem'
      }}>
        {/* Brand Logo */}
        <Link to="/" style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
          <div style={{
            background: 'var(--accent-gradient)',
            width: '38px',
            height: '38px',
            borderRadius: '10px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            boxShadow: 'var(--shadow-glow)'
          }}>
            <Briefcase size={22} />
          </div>
          <span style={{ fontSize: '1.35rem', fontWeight: 800, background: 'var(--accent-gradient)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
            Smart Hire
          </span>
        </Link>

        {/* Links */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '1.25rem', fontWeight: 500, flexWrap: 'wrap' }}>
          <Link to="/jobs" style={{ color: 'var(--text-secondary)', transition: 'color 0.2s', fontSize: '0.95rem' }}>
            Find Jobs
          </Link>

          <Link to="/companies" style={{ color: 'var(--text-secondary)', transition: 'color 0.2s', fontSize: '0.95rem', display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
            <Building2 size={16} /> Companies
          </Link>

          {user && user.role === 'JOB_SEEKER' && (
            <Link to="/dashboard/seeker" style={{ color: 'var(--text-secondary)', fontSize: '0.95rem' }}>
              My Dashboard
            </Link>
          )}

          {user && user.role === 'RECRUITER' && (
            <>
              <Link to="/dashboard/recruiter" style={{ color: 'var(--text-secondary)', fontSize: '0.95rem' }}>
                Recruiter Dashboard
              </Link>
              <Link to="/dashboard/recruiter?action=post" className="btn-primary btn-sm" style={{ display: 'flex', alignItems: 'center', gap: '0.35rem', padding: '0.35rem 0.85rem' }}>
                + Post a Job
              </Link>
            </>
          )}

          {user && user.role === 'ADMIN' && (
            <Link to="/dashboard/admin" style={{ color: 'var(--text-secondary)', fontSize: '0.95rem' }}>
              Admin Panel
            </Link>
          )}

          {/* Theme Toggle Button (Dark / Bright Mode) */}
          <button
            onClick={toggleTheme}
            title={theme === 'dark' ? 'Switch to Bright Mode' : 'Switch to Dark Mode'}
            style={{
              background: 'var(--bg-glass)',
              border: '1px solid var(--border-color)',
              padding: '0.45rem',
              borderRadius: 'var(--radius-md)',
              color: theme === 'dark' ? '#fbbf24' : '#6366f1',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              cursor: 'pointer',
              transition: 'all 0.2s'
            }}>
            {theme === 'dark' ? <Sun size={18} /> : <Moon size={18} />}
          </button>

          {user ? (
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
              {/* Notification Bell */}
              <NotificationBell />

              <div style={{ position: 'relative' }}>
                <button
                  onClick={() => setDropdownOpen(!dropdownOpen)}
                  style={{
                    background: 'var(--bg-glass)',
                    border: '1px solid var(--border-color)',
                    padding: '0.4rem 0.85rem',
                    borderRadius: 'var(--radius-md)',
                    color: 'var(--text-primary)',
                    display: 'flex',
                    alignItems: 'center',
                    gap: '0.5rem',
                    cursor: 'pointer'
                  }}>
                  <User size={18} color="var(--accent-primary)" />
                  <span style={{ fontSize: '0.9rem' }}>{user.fullName}</span>
                  <ChevronDown size={16} />
                </button>

                {dropdownOpen && (
                  <div style={{
                    position: 'absolute',
                    right: 0,
                    marginTop: '0.5rem',
                    width: '200px',
                    background: 'var(--bg-secondary)',
                    border: '1px solid var(--border-color)',
                    borderRadius: 'var(--radius-md)',
                    boxShadow: 'var(--shadow-card)',
                    padding: '0.5rem',
                    zIndex: 200
                  }}>
                    <div style={{ padding: '0.5rem', fontSize: '0.8rem', color: 'var(--text-muted)', borderBottom: '1px solid var(--border-color)' }}>
                      Signed in as <strong>{user.role}</strong>
                    </div>
                    <button
                      onClick={handleLogout}
                      style={{
                        width: '100%',
                        textAlign: 'left',
                        padding: '0.6rem 0.75rem',
                        background: 'none',
                        color: 'var(--danger-color)',
                        display: 'flex',
                        alignItems: 'center',
                        gap: '0.5rem',
                        fontSize: '0.9rem',
                        cursor: 'pointer',
                        border: 'none'
                      }}>
                      <LogOut size={16} /> Logout
                    </button>
                  </div>
                )}
              </div>
            </div>
          ) : (
            <div style={{ display: 'flex', gap: '0.75rem' }}>
              <Link to="/login" className="btn-secondary btn-sm">Login</Link>
              <Link to="/register" className="btn-primary btn-sm">Register</Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
