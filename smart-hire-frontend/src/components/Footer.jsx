import React from 'react';
import { Briefcase, Heart } from 'lucide-react';

const Footer = () => {
  return (
    <footer style={{
      background: 'var(--bg-secondary)',
      borderTop: '1px solid var(--border-color)',
      padding: '3rem 2rem 2rem 2rem',
      marginTop: '4rem'
    }}>
      <div style={{
        maxWidth: '1280px',
        margin: '0 auto',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: '1.5rem',
        textAlign: 'center'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
          <div style={{ background: 'var(--accent-gradient)', padding: '0.5rem', borderRadius: '8px', color: 'white' }}>
            <Briefcase size={20} />
          </div>
          <span style={{ fontSize: '1.3rem', fontWeight: 800 }}>Smart Hire</span>
        </div>
        <p style={{ color: 'var(--text-secondary)', maxWidth: '600px', fontSize: '0.95rem' }}>
          Production-style AI-Powered Job Portal & Candidate Screening System built with Java 21, Spring Boot 3, Spring Security JWT, React.js, MySQL & Docker Compose.
        </p>
        <div style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>
          © 2026 Smart Hire Engine. Built for Serious SDE Full Stack Engineering.
        </div>
      </div>
    </footer>
  );
};

export default Footer;
