import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Mail, Lock, LogIn, AlertCircle } from 'lucide-react';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const user = await login(email, password);
      if (user.role === 'ADMIN') navigate('/dashboard/admin');
      else if (user.role === 'RECRUITER') navigate('/dashboard/recruiter');
      else navigate('/dashboard/seeker');
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid email or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '440px', margin: '4rem auto', padding: '0 1rem' }}>
      <div className="glass-card" style={{ padding: '2.5rem 2rem' }}>
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <h2 style={{ fontSize: '1.8rem', marginBottom: '0.5rem' }}>Welcome Back</h2>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.95rem' }}>Sign in to your Smart Hire account</p>
        </div>

        {error && (
          <div style={{ background: 'rgba(239, 68, 68, 0.15)', border: '1px solid rgba(239, 68, 68, 0.4)', color: '#f87171', padding: '0.75rem 1rem', borderRadius: 'var(--radius-md)', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '0.9rem' }}>
            <AlertCircle size={18} />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
          <div>
            <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>
              Email Address
            </label>
            <div style={{ position: 'relative' }}>
              <Mail size={18} style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
              <input
                type="email"
                required
                className="input-field"
                style={{ paddingLeft: '2.75rem' }}
                placeholder="name@company.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>
              Password
            </label>
            <div style={{ position: 'relative' }}>
              <Lock size={18} style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
              <input
                type="password"
                required
                className="input-field"
                style={{ paddingLeft: '2.75rem' }}
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>

          <button type="submit" className="btn-primary" disabled={loading} style={{ width: '100%', marginTop: '0.5rem' }}>
            <LogIn size={18} /> {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>

        <div style={{ textAlign: 'center', marginTop: '1.5rem', fontSize: '0.9rem', color: 'var(--text-secondary)' }}>
          Don't have an account?{' '}
          <Link to="/register" style={{ color: 'var(--accent-primary)', fontWeight: 600 }}>
            Register Now
          </Link>
        </div>

        {/* Quick Demo Logins */}
        <div style={{ marginTop: '2rem', paddingTop: '1.5rem', borderTop: '1px solid var(--border-color)', fontSize: '0.8rem', color: 'var(--text-muted)' }}>
          <div style={{ fontWeight: 600, marginBottom: '0.75rem', color: 'var(--text-secondary)' }}>
            Quick Demo Auto-Fill (Click to login as):
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.5rem' }}>
            <button
              type="button"
              onClick={() => { setEmail('candidate@smarthire.ai'); setPassword('password123'); }}
              style={{ padding: '0.4rem 0.6rem', fontSize: '0.75rem', background: 'rgba(99, 102, 241, 0.15)', border: '1px solid rgba(99, 102, 241, 0.3)', color: '#a5b4fc', borderRadius: '6px', textAlign: 'left', cursor: 'pointer' }}>
              👤 Job Seeker (Rahul)
            </button>
            <button
              type="button"
              onClick={() => { setEmail('admin@smarthire.ai'); setPassword('password123'); }}
              style={{ padding: '0.4rem 0.6rem', fontSize: '0.75rem', background: 'rgba(239, 68, 68, 0.15)', border: '1px solid rgba(239, 68, 68, 0.3)', color: '#fca5a5', borderRadius: '6px', textAlign: 'left', cursor: 'pointer' }}>
              🛡️ Admin (System)
            </button>
            <button
              type="button"
              onClick={() => { setEmail('recruiter@google.com'); setPassword('password123'); }}
              style={{ padding: '0.4rem 0.6rem', fontSize: '0.75rem', background: 'rgba(16, 185, 129, 0.15)', border: '1px solid rgba(16, 185, 129, 0.3)', color: '#6ee7b7', borderRadius: '6px', textAlign: 'left', cursor: 'pointer' }}>
              🏢 Google
            </button>
            <button
              type="button"
              onClick={() => { setEmail('recruiter@microsoft.com'); setPassword('password123'); }}
              style={{ padding: '0.4rem 0.6rem', fontSize: '0.75rem', background: 'rgba(59, 130, 246, 0.15)', border: '1px solid rgba(59, 130, 246, 0.3)', color: '#93c5fd', borderRadius: '6px', textAlign: 'left', cursor: 'pointer' }}>
              🏢 Microsoft
            </button>
            <button
              type="button"
              onClick={() => { setEmail('recruiter@amazon.com'); setPassword('password123'); }}
              style={{ padding: '0.4rem 0.6rem', fontSize: '0.75rem', background: 'rgba(245, 158, 11, 0.15)', border: '1px solid rgba(245, 158, 11, 0.3)', color: '#fcd34d', borderRadius: '6px', textAlign: 'left', cursor: 'pointer' }}>
              🏢 Amazon
            </button>
            <button
              type="button"
              onClick={() => { setEmail('recruiter@razorpay.com'); setPassword('password123'); }}
              style={{ padding: '0.4rem 0.6rem', fontSize: '0.75rem', background: 'rgba(168, 85, 247, 0.15)', border: '1px solid rgba(168, 85, 247, 0.3)', color: '#d8b4fe', borderRadius: '6px', textAlign: 'left', cursor: 'pointer' }}>
              🏢 Razorpay
            </button>
            <button
              type="button"
              onClick={() => { setEmail('recruiter@swiggy.com'); setPassword('password123'); }}
              style={{ padding: '0.4rem 0.6rem', fontSize: '0.75rem', background: 'rgba(249, 115, 22, 0.15)', border: '1px solid rgba(249, 115, 22, 0.3)', color: '#fdba74', borderRadius: '6px', textAlign: 'left', cursor: 'pointer' }}>
              🏢 Swiggy
            </button>
            <button
              type="button"
              onClick={() => { setEmail('recruiter@cred.club'); setPassword('password123'); }}
              style={{ padding: '0.4rem 0.6rem', fontSize: '0.75rem', background: 'rgba(236, 72, 153, 0.15)', border: '1px solid rgba(236, 72, 153, 0.3)', color: '#f472b6', borderRadius: '6px', textAlign: 'left', cursor: 'pointer' }}>
              🏢 CRED
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
