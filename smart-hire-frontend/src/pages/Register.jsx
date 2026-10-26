import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { User, Mail, Lock, Phone, Building, UserCheck, AlertCircle } from 'lucide-react';

const Register = () => {
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    password: '',
    phone: '',
    role: 'JOB_SEEKER',
    companyName: '',
    designation: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const user = await register(formData);
      if (user.role === 'RECRUITER') navigate('/dashboard/recruiter');
      else navigate('/dashboard/seeker');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '520px', margin: '3rem auto', padding: '0 1rem' }}>
      <div className="glass-card" style={{ padding: '2.5rem 2rem' }}>
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <h2 style={{ fontSize: '1.8rem', marginBottom: '0.5rem' }}>Create an Account</h2>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.95rem' }}>Join Smart Hire as a Job Seeker or Recruiter</p>
        </div>

        {error && (
          <div style={{ background: 'rgba(239, 68, 68, 0.15)', border: '1px solid rgba(239, 68, 68, 0.4)', color: '#f87171', padding: '0.75rem 1rem', borderRadius: 'var(--radius-md)', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '0.9rem' }}>
            <AlertCircle size={18} />
            <span>{error}</span>
          </div>
        )}

        {/* Role Selector Tabs */}
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.5rem', marginBottom: '1.5rem', background: 'rgba(0, 0, 0, 0.2)', padding: '0.35rem', borderRadius: 'var(--radius-md)' }}>
          <button
            type="button"
            onClick={() => setFormData({ ...formData, role: 'JOB_SEEKER' })}
            style={{
              padding: '0.6rem',
              borderRadius: 'var(--radius-sm)',
              fontWeight: 600,
              fontSize: '0.9rem',
              background: formData.role === 'JOB_SEEKER' ? 'var(--accent-primary)' : 'transparent',
              color: formData.role === 'JOB_SEEKER' ? 'white' : 'var(--text-secondary)',
              transition: 'all 0.2s'
            }}>
            Job Seeker
          </button>
          <button
            type="button"
            onClick={() => setFormData({ ...formData, role: 'RECRUITER' })}
            style={{
              padding: '0.6rem',
              borderRadius: 'var(--radius-sm)',
              fontWeight: 600,
              fontSize: '0.9rem',
              background: formData.role === 'RECRUITER' ? 'var(--accent-primary)' : 'transparent',
              color: formData.role === 'RECRUITER' ? 'white' : 'var(--text-secondary)',
              transition: 'all 0.2s'
            }}>
            Recruiter
          </button>
        </div>

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <div>
            <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Full Name</label>
            <input type="text" name="fullName" required className="input-field" value={formData.fullName} onChange={handleChange} placeholder="e.g. Rahul Sharma" />
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Email Address</label>
            <input type="email" name="email" required className="input-field" value={formData.email} onChange={handleChange} placeholder="name@domain.com" />
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Password</label>
            <input type="password" name="password" required minLength={6} className="input-field" value={formData.password} onChange={handleChange} placeholder="At least 6 characters" />
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Phone Number</label>
            <input type="text" name="phone" className="input-field" value={formData.phone} onChange={handleChange} placeholder="+91-9876543210" />
          </div>

          {formData.role === 'RECRUITER' && (
            <>
              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Company Name</label>
                <input type="text" name="companyName" required className="input-field" value={formData.companyName} onChange={handleChange} placeholder="e.g. Razorpay, Flipkart" />
              </div>
              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Designation</label>
                <input type="text" name="designation" className="input-field" value={formData.designation} onChange={handleChange} placeholder="e.g. Tech Recruiter / HR Lead" />
              </div>
            </>
          )}

          <button type="submit" className="btn-primary" disabled={loading} style={{ width: '100%', marginTop: '0.5rem' }}>
            <UserCheck size={18} /> {loading ? 'Registering...' : 'Create Account'}
          </button>
        </form>

        <div style={{ textAlign: 'center', marginTop: '1.5rem', fontSize: '0.9rem', color: 'var(--text-secondary)' }}>
          Already have an account?{' '}
          <Link to="/login" style={{ color: 'var(--accent-primary)', fontWeight: 600 }}>
            Sign In
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Register;
