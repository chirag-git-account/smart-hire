import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { ShieldCheck, Users, Building2, Briefcase, Sparkles, UserX, UserCheck, Search, Filter, Trash2, Eye, CheckCircle2, XCircle, ChevronRight, AlertTriangle, ExternalLink } from 'lucide-react';
import { useToast } from '../components/ToastProvider';
import AiScoreBadge from '../components/AiScoreBadge';

const AdminDashboard = () => {
  const toast = useToast();
  const [activeTab, setActiveTab] = useState('overview'); // overview, users, companies, jobs, applications

  // Stats State
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  // Users State
  const [users, setUsers] = useState([]);

  // Companies State
  const [companies, setCompanies] = useState([]);
  const [companySearch, setCompanySearch] = useState('');
  const [companyStatusFilter, setCompanyStatusFilter] = useState('ALL');

  // Jobs State
  const [jobs, setJobs] = useState([]);
  const [jobSearch, setJobSearch] = useState('');
  const [jobCompanyFilter, setJobCompanyFilter] = useState('ALL');
  const [jobStatusFilter, setJobStatusFilter] = useState('ALL');

  // Applications State
  const [applications, setApplications] = useState([]);
  const [appSearch, setAppSearch] = useState('');
  const [appStatusFilter, setAppStatusFilter] = useState('ALL');

  // Selected Item Modals
  const [selectedCompany, setSelectedCompany] = useState(null);
  const [selectedJob, setSelectedJob] = useState(null);

  useEffect(() => {
    fetchStats();
    fetchUsers();
    fetchCompanies();
    fetchJobs();
    fetchApplications();
  }, []);

  const fetchStats = async () => {
    try {
      const res = await api.get('/admin/stats');
      setStats(res.data);
    } catch (e) {
      console.error('Failed to load admin stats', e);
    } finally {
      setLoading(false);
    }
  };

  const fetchUsers = async () => {
    try {
      const res = await api.get('/admin/users');
      setUsers(res.data);
    } catch (e) {
      console.error('Failed to load users', e);
    }
  };

  const fetchCompanies = async () => {
    try {
      const activeParam = companyStatusFilter === 'ALL' ? '' : `&active=${companyStatusFilter === 'ACTIVE'}`;
      const res = await api.get(`/admin/companies?keyword=${encodeURIComponent(companySearch)}${activeParam}&size=50`);
      setCompanies(res.data.content || res.data || []);
    } catch (e) {
      console.error('Failed to load companies', e);
    }
  };

  const fetchJobs = async () => {
    try {
      const activeParam = jobStatusFilter === 'ALL' ? '' : `&active=${jobStatusFilter === 'ACTIVE'}`;
      const compParam = jobCompanyFilter === 'ALL' ? '' : `&company=${encodeURIComponent(jobCompanyFilter)}`;
      const res = await api.get(`/admin/jobs?keyword=${encodeURIComponent(jobSearch)}${compParam}${activeParam}&size=50`);
      setJobs(res.data.content || res.data || []);
    } catch (e) {
      console.error('Failed to load jobs', e);
    }
  };

  const fetchApplications = async () => {
    try {
      const statusParam = appStatusFilter === 'ALL' ? '' : `&status=${appStatusFilter}`;
      const res = await api.get(`/admin/applications?keyword=${encodeURIComponent(appSearch)}${statusParam}&size=50`);
      setApplications(res.data.content || res.data || []);
    } catch (e) {
      console.error('Failed to load applications', e);
    }
  };

  // User Actions
  const handleToggleUserStatus = async (userId, currentStatus) => {
    const nextStatus = currentStatus === 'ACTIVE' ? 'SUSPENDED' : 'ACTIVE';
    try {
      await api.patch(`/admin/users/${userId}/status?status=${nextStatus}`);
      toast.success(`User status changed to ${nextStatus}`);
      fetchUsers();
      fetchStats();
    } catch (e) {
      toast.error('Failed to update user status');
    }
  };

  // Company Actions
  const handleToggleCompanyStatus = async (companyId, currentActive) => {
    try {
      await api.patch(`/admin/companies/${companyId}/status?active=${!currentActive}`);
      toast.success(`Company ${!currentActive ? 'Activated' : 'Suspended'}`);
      fetchCompanies();
      fetchStats();
    } catch (e) {
      toast.error('Failed to update company status');
    }
  };

  const handleDeleteCompany = async (companyId, companyName) => {
    if (!window.confirm(`Are you sure you want to delete "${companyName}"? This will deactivate all associated jobs.`)) return;
    try {
      await api.delete(`/admin/companies/${companyId}`);
      toast.success('Company deleted successfully');
      fetchCompanies();
      fetchStats();
    } catch (e) {
      toast.error('Failed to delete company');
    }
  };

  // Job Actions
  const handleToggleJobStatus = async (jobId, currentActive) => {
    try {
      await api.patch(`/admin/jobs/${jobId}/status?active=${!currentActive}`);
      toast.success(`Job ${!currentActive ? 'Activated' : 'Deactivated'}`);
      fetchJobs();
      fetchStats();
    } catch (e) {
      toast.error('Failed to update job status');
    }
  };

  const handleDeleteJob = async (jobId, jobTitle) => {
    if (!window.confirm(`Are you sure you want to remove job "${jobTitle}"?`)) return;
    try {
      await api.delete(`/admin/jobs/${jobId}`);
      toast.success('Job removed successfully');
      fetchJobs();
      fetchStats();
    } catch (e) {
      toast.error('Failed to delete job');
    }
  };

  // Application Actions
  const handleUpdateAppStatus = async (appId, newStatus) => {
    try {
      await api.patch(`/admin/applications/${appId}/status?status=${newStatus}`);
      toast.success(`Application updated to ${newStatus}`);
      fetchApplications();
      fetchStats();
    } catch (e) {
      toast.error('Failed to update application status');
    }
  };

  if (loading) return <div style={{ textAlign: 'center', padding: '4rem', color: 'var(--text-muted)' }}>Loading Admin Portal...</div>;

  return (
    <div style={{ maxWidth: '1380px', margin: '2rem auto', padding: '0 1.5rem' }}>
      {/* Header */}
      <div style={{ marginBottom: '2rem' }}>
        <h1 style={{ fontSize: '2.2rem', marginBottom: '0.4rem', display: 'flex', alignItems: 'center', gap: '0.6rem' }}>
          <ShieldCheck size={32} color="var(--accent-primary)" /> System Administration Portal
        </h1>
        <p style={{ color: 'var(--text-secondary)' }}>Full platform control: Users, Hiring Companies, Job Postings, and AI Pipeline Monitoring.</p>
      </div>

      {/* Metrics Row */}
      {stats && (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '1rem', marginBottom: '2rem' }}>
          <div className="glass-card" style={{ padding: '1.25rem' }}>
            <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem', fontWeight: 700 }}>TOTAL USERS</div>
            <div style={{ fontSize: '1.8rem', fontWeight: 800 }}>{stats.totalUsers}</div>
          </div>
          <div className="glass-card" style={{ padding: '1.25rem' }}>
            <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem', fontWeight: 700 }}>JOB SEEKERS</div>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#60a5fa' }}>{stats.totalJobSeekers}</div>
          </div>
          <div className="glass-card" style={{ padding: '1.25rem' }}>
            <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem', fontWeight: 700 }}>RECRUITERS</div>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#c084fc' }}>{stats.totalRecruiters}</div>
          </div>
          <div className="glass-card" style={{ padding: '1.25rem' }}>
            <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem', fontWeight: 700 }}>COMPANIES</div>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#34d399' }}>{stats.totalCompanies}</div>
          </div>
          <div className="glass-card" style={{ padding: '1.25rem' }}>
            <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem', fontWeight: 700 }}>POSTED JOBS</div>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#38bdf8' }}>{stats.totalJobs}</div>
          </div>
          <div className="glass-card" style={{ padding: '1.25rem' }}>
            <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem', fontWeight: 700 }}>APPLICATIONS</div>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#a78bfa' }}>{stats.totalApplications || 0}</div>
          </div>
          <div className="glass-card" style={{ padding: '1.25rem' }}>
            <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem', fontWeight: 700 }}>AI SCORING RUNS</div>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: '#f59e0b' }}>{stats.totalAiAnalyses}</div>
          </div>
        </div>
      )}

      {/* Tabs */}
      <div style={{ display: 'flex', gap: '0.5rem', marginBottom: '1.75rem', borderBottom: '1px solid var(--border-color)', paddingBottom: '0.5rem', flexWrap: 'wrap' }}>
        <button
          onClick={() => setActiveTab('overview')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'overview' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'overview' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          Overview
        </button>

        <button
          onClick={() => setActiveTab('companies')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'companies' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'companies' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          🏢 Companies ({companies.length})
        </button>

        <button
          onClick={() => setActiveTab('jobs')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'jobs' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'jobs' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          💼 Jobs ({jobs.length})
        </button>

        <button
          onClick={() => setActiveTab('applications')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'applications' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'applications' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          📝 Applications ({applications.length})
        </button>

        <button
          onClick={() => setActiveTab('users')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'users' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'users' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          👥 Users ({users.length})
        </button>
      </div>

      {/* ======================================================== */}
      {/* TAB 1: OVERVIEW */}
      {/* ======================================================== */}
      {activeTab === 'overview' && (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '1.5rem' }}>
          <div className="glass-card" style={{ padding: '1.75rem' }}>
            <h3 style={{ fontSize: '1.2rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Building2 size={20} color="#34d399" /> Hiring Ecosystem Status
            </h3>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', lineHeight: 1.5, marginBottom: '1.25rem' }}>
              <strong>{companies.length} hiring partners</strong> actively publishing engineering roles across FinTech, Cloud, AI, and E-Commerce.
            </p>
            <button className="btn-secondary btn-sm" onClick={() => setActiveTab('companies')}>
              Manage Companies <ChevronRight size={15} />
            </button>
          </div>

          <div className="glass-card" style={{ padding: '1.75rem' }}>
            <h3 style={{ fontSize: '1.2rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Briefcase size={20} color="#38bdf8" /> Job Pipeline Health
            </h3>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', lineHeight: 1.5, marginBottom: '1.25rem' }}>
              <strong>{jobs.length} active listings</strong>. Admins can audit salaries, requirements, and deactivate flagged postings.
            </p>
            <button className="btn-secondary btn-sm" onClick={() => setActiveTab('jobs')}>
              Manage Jobs <ChevronRight size={15} />
            </button>
          </div>

          <div className="glass-card" style={{ padding: '1.75rem' }}>
            <h3 style={{ fontSize: '1.2rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Sparkles size={20} color="#f59e0b" /> AI Matching Engine
            </h3>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', lineHeight: 1.5, marginBottom: '1.25rem' }}>
              <strong>{stats?.totalAiAnalyses || 0} automated assessments</strong> scored with 4-dimension weighted algorithm.
            </p>
            <button className="btn-secondary btn-sm" onClick={() => setActiveTab('applications')}>
              Review Applications <ChevronRight size={15} />
            </button>
          </div>
        </div>
      )}

      {/* ======================================================== */}
      {/* TAB 2: COMPANY MANAGEMENT */}
      {/* ======================================================== */}
      {activeTab === 'companies' && (
        <div className="glass-card" style={{ padding: '1.75rem' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem', flexWrap: 'wrap', gap: '1rem' }}>
            <h2 style={{ fontSize: '1.35rem', margin: 0 }}>Company Directory & Moderation</h2>

            <div style={{ display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
              <div style={{ position: 'relative' }}>
                <Search size={16} color="var(--text-muted)" style={{ position: 'absolute', left: '0.75rem', top: '50%', transform: 'translateY(-50%)' }} />
                <input
                  type="text"
                  placeholder="Search company..."
                  className="input-field"
                  style={{ paddingLeft: '2.2rem', width: '220px', height: '36px', fontSize: '0.85rem' }}
                  value={companySearch}
                  onChange={(e) => setCompanySearch(e.target.value)}
                  onKeyDown={(e) => e.key === 'Enter' && fetchCompanies()}
                />
              </div>

              <select
                className="input-field"
                style={{ height: '36px', fontSize: '0.85rem', width: '130px' }}
                value={companyStatusFilter}
                onChange={(e) => { setCompanyStatusFilter(e.target.value); setTimeout(fetchCompanies, 50); }}>
                <option value="ALL">All Status</option>
                <option value="ACTIVE">Active</option>
                <option value="INACTIVE">Suspended</option>
              </select>

              <button className="btn-secondary btn-sm" onClick={fetchCompanies}>Search</button>
            </div>
          </div>

          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
              <thead>
                <tr style={{ borderBottom: '1px solid var(--border-color)', color: 'var(--text-muted)', fontSize: '0.82rem' }}>
                  <th style={{ padding: '0.75rem 1rem' }}>COMPANY</th>
                  <th style={{ padding: '0.75rem 1rem' }}>INDUSTRY</th>
                  <th style={{ padding: '0.75rem 1rem' }}>LOCATION</th>
                  <th style={{ padding: '0.75rem 1rem' }}>OWNER / RECRUITER</th>
                  <th style={{ padding: '0.75rem 1rem' }}>JOBS</th>
                  <th style={{ padding: '0.75rem 1rem' }}>STATUS</th>
                  <th style={{ padding: '0.75rem 1rem', textAlign: 'right' }}>ACTIONS</th>
                </tr>
              </thead>
              <tbody>
                {companies.map((c) => (
                  <tr key={c.id} style={{ borderBottom: '1px solid var(--border-color)' }}>
                    <td style={{ padding: '1rem', fontWeight: 700 }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem' }}>
                        <div style={{ width: '32px', height: '32px', borderRadius: '8px', background: 'rgba(99, 102, 241, 0.15)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#818cf8' }}>
                          <Building2 size={16} />
                        </div>
                        <div>
                          <div>{c.name}</div>
                          {c.website && <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{c.website.replace('https://', '')}</span>}
                        </div>
                      </div>
                    </td>
                    <td style={{ padding: '1rem', color: 'var(--text-secondary)', fontSize: '0.85rem' }}>{c.industry || 'Technology'}</td>
                    <td style={{ padding: '1rem', color: 'var(--text-secondary)', fontSize: '0.85rem' }}>{c.location || 'India'}</td>
                    <td style={{ padding: '1rem', fontSize: '0.85rem' }}>
                      <div>{c.ownerName || 'System'}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{c.ownerEmail}</div>
                    </td>
                    <td style={{ padding: '1rem', fontSize: '0.85rem' }}>
                      <span style={{ fontWeight: 700, color: '#38bdf8' }}>{c.activeJobCount} active</span> / {c.totalJobCount} total
                    </td>
                    <td style={{ padding: '1rem' }}>
                      <span style={{
                        background: c.isActive ? 'rgba(16, 185, 129, 0.15)' : 'rgba(239, 68, 68, 0.15)',
                        color: c.isActive ? '#34d399' : '#f87171',
                        padding: '0.2rem 0.5rem',
                        borderRadius: '4px',
                        fontSize: '0.75rem',
                        fontWeight: 700
                      }}>
                        {c.isActive ? 'ACTIVE' : 'SUSPENDED'}
                      </span>
                    </td>
                    <td style={{ padding: '1rem', textAlign: 'right' }}>
                      <div style={{ display: 'flex', gap: '0.4rem', justifyContent: 'flex-end' }}>
                        <button
                          className="btn-secondary btn-sm"
                          onClick={() => handleToggleCompanyStatus(c.id, c.isActive)}
                          style={{ color: c.isActive ? '#f87171' : '#34d399', fontSize: '0.75rem', padding: '0.3rem 0.6rem' }}>
                          {c.isActive ? 'Suspend' : 'Activate'}
                        </button>
                        <button
                          className="btn-secondary btn-sm"
                          onClick={() => handleDeleteCompany(c.id, c.name)}
                          style={{ color: '#f87171', borderColor: 'rgba(239, 68, 68, 0.3)', padding: '0.3rem 0.6rem' }}>
                          <Trash2 size={13} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* ======================================================== */}
      {/* TAB 3: JOB MANAGEMENT */}
      {/* ======================================================== */}
      {activeTab === 'jobs' && (
        <div className="glass-card" style={{ padding: '1.75rem' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem', flexWrap: 'wrap', gap: '1rem' }}>
            <h2 style={{ fontSize: '1.35rem', margin: 0 }}>Platform Job Listings Moderation</h2>

            <div style={{ display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
              <div style={{ position: 'relative' }}>
                <Search size={16} color="var(--text-muted)" style={{ position: 'absolute', left: '0.75rem', top: '50%', transform: 'translateY(-50%)' }} />
                <input
                  type="text"
                  placeholder="Search job title..."
                  className="input-field"
                  style={{ paddingLeft: '2.2rem', width: '220px', height: '36px', fontSize: '0.85rem' }}
                  value={jobSearch}
                  onChange={(e) => setJobSearch(e.target.value)}
                  onKeyDown={(e) => e.key === 'Enter' && fetchJobs()}
                />
              </div>

              <select
                className="input-field"
                style={{ height: '36px', fontSize: '0.85rem', width: '130px' }}
                value={jobStatusFilter}
                onChange={(e) => { setJobStatusFilter(e.target.value); setTimeout(fetchJobs, 50); }}>
                <option value="ALL">All Status</option>
                <option value="ACTIVE">Active</option>
                <option value="INACTIVE">Deactivated</option>
              </select>

              <button className="btn-secondary btn-sm" onClick={fetchJobs}>Search</button>
            </div>
          </div>

          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
              <thead>
                <tr style={{ borderBottom: '1px solid var(--border-color)', color: 'var(--text-muted)', fontSize: '0.82rem' }}>
                  <th style={{ padding: '0.75rem 1rem' }}>JOB TITLE</th>
                  <th style={{ padding: '0.75rem 1rem' }}>COMPANY</th>
                  <th style={{ padding: '0.75rem 1rem' }}>LOCATION / TYPE</th>
                  <th style={{ padding: '0.75rem 1rem' }}>SALARY RANGE</th>
                  <th style={{ padding: '0.75rem 1rem' }}>APPLICANTS</th>
                  <th style={{ padding: '0.75rem 1rem' }}>STATUS</th>
                  <th style={{ padding: '0.75rem 1rem', textAlign: 'right' }}>ACTIONS</th>
                </tr>
              </thead>
              <tbody>
                {jobs.map((j) => (
                  <tr key={j.id} style={{ borderBottom: '1px solid var(--border-color)' }}>
                    <td style={{ padding: '1rem', fontWeight: 700 }}>
                      <div>{j.title}</div>
                      <div style={{ display: 'flex', gap: '0.3rem', marginTop: '0.3rem', flexWrap: 'wrap' }}>
                        {j.requiredSkills?.slice(0, 3).map((sk, idx) => (
                          <span key={idx} style={{ background: 'rgba(99, 102, 241, 0.12)', color: '#a5b4fc', fontSize: '0.7rem', padding: '0.1rem 0.35rem', borderRadius: '3px' }}>
                            {sk}
                          </span>
                        ))}
                      </div>
                    </td>
                    <td style={{ padding: '1rem', color: '#34d399', fontWeight: 600, fontSize: '0.85rem' }}>{j.companyName}</td>
                    <td style={{ padding: '1rem', color: 'var(--text-secondary)', fontSize: '0.85rem' }}>{j.location} • {j.jobType}</td>
                    <td style={{ padding: '1rem', fontSize: '0.85rem' }}>
                      {j.salaryMax ? `₹${(j.salaryMin / 100000).toFixed(1)}L - ₹${(j.salaryMax / 100000).toFixed(1)}L` : 'Negotiable'}
                    </td>
                    <td style={{ padding: '1rem', fontSize: '0.85rem', fontWeight: 700, color: '#818cf8' }}>
                      {j.applicationCount || 0} candidates
                    </td>
                    <td style={{ padding: '1rem' }}>
                      <span style={{
                        background: j.isActive ? 'rgba(16, 185, 129, 0.15)' : 'rgba(239, 68, 68, 0.15)',
                        color: j.isActive ? '#34d399' : '#f87171',
                        padding: '0.2rem 0.5rem',
                        borderRadius: '4px',
                        fontSize: '0.75rem',
                        fontWeight: 700
                      }}>
                        {j.isActive ? 'ACTIVE' : 'DEACTIVATED'}
                      </span>
                    </td>
                    <td style={{ padding: '1rem', textAlign: 'right' }}>
                      <div style={{ display: 'flex', gap: '0.4rem', justifyContent: 'flex-end' }}>
                        <button
                          className="btn-secondary btn-sm"
                          onClick={() => handleToggleJobStatus(j.id, j.isActive)}
                          style={{ color: j.isActive ? '#f87171' : '#34d399', fontSize: '0.75rem', padding: '0.3rem 0.6rem' }}>
                          {j.isActive ? 'Deactivate' : 'Activate'}
                        </button>
                        <button
                          className="btn-secondary btn-sm"
                          onClick={() => handleDeleteJob(j.id, j.title)}
                          style={{ color: '#f87171', borderColor: 'rgba(239, 68, 68, 0.3)', padding: '0.3rem 0.6rem' }}>
                          <Trash2 size={13} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* ======================================================== */}
      {/* TAB 4: APPLICATION MANAGEMENT */}
      {/* ======================================================== */}
      {activeTab === 'applications' && (
        <div className="glass-card" style={{ padding: '1.75rem' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem', flexWrap: 'wrap', gap: '1rem' }}>
            <h2 style={{ fontSize: '1.35rem', margin: 0 }}>Hiring Pipeline & AI Assessments</h2>

            <div style={{ display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
              <div style={{ position: 'relative' }}>
                <Search size={16} color="var(--text-muted)" style={{ position: 'absolute', left: '0.75rem', top: '50%', transform: 'translateY(-50%)' }} />
                <input
                  type="text"
                  placeholder="Search candidate, job, company..."
                  className="input-field"
                  style={{ paddingLeft: '2.2rem', width: '250px', height: '36px', fontSize: '0.85rem' }}
                  value={appSearch}
                  onChange={(e) => setAppSearch(e.target.value)}
                  onKeyDown={(e) => e.key === 'Enter' && fetchApplications()}
                />
              </div>

              <select
                className="input-field"
                style={{ height: '36px', fontSize: '0.85rem', width: '150px' }}
                value={appStatusFilter}
                onChange={(e) => { setAppStatusFilter(e.target.value); setTimeout(fetchApplications, 50); }}>
                <option value="ALL">All Status</option>
                <option value="APPLIED">Applied</option>
                <option value="SHORTLISTED">Shortlisted</option>
                <option value="INTERVIEW_SCHEDULED">Interview</option>
                <option value="HIRED">Hired</option>
                <option value="REJECTED">Rejected</option>
              </select>

              <button className="btn-secondary btn-sm" onClick={fetchApplications}>Search</button>
            </div>
          </div>

          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
              <thead>
                <tr style={{ borderBottom: '1px solid var(--border-color)', color: 'var(--text-muted)', fontSize: '0.82rem' }}>
                  <th style={{ padding: '0.75rem 1rem' }}>CANDIDATE</th>
                  <th style={{ padding: '0.75rem 1rem' }}>TARGET ROLE</th>
                  <th style={{ padding: '0.75rem 1rem' }}>COMPANY</th>
                  <th style={{ padding: '0.75rem 1rem' }}>AI MATCH SCORE</th>
                  <th style={{ padding: '0.75rem 1rem' }}>STATUS</th>
                  <th style={{ padding: '0.75rem 1rem' }}>APPLIED ON</th>
                  <th style={{ padding: '0.75rem 1rem', textAlign: 'right' }}>OVERRIDE STATUS</th>
                </tr>
              </thead>
              <tbody>
                {applications.map((a) => (
                  <tr key={a.id} style={{ borderBottom: '1px solid var(--border-color)' }}>
                    <td style={{ padding: '1rem', fontWeight: 700 }}>
                      <div>{a.candidateName}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Resume: {a.resumeFileName || 'resume.pdf'}</div>
                    </td>
                    <td style={{ padding: '1rem', color: 'var(--text-primary)', fontSize: '0.85rem' }}>{a.jobTitle}</td>
                    <td style={{ padding: '1rem', color: '#34d399', fontWeight: 600, fontSize: '0.85rem' }}>{a.companyName}</td>
                    <td style={{ padding: '1rem' }}>
                      <AiScoreBadge score={a.aiScore} recommendation={a.aiRecommendation} />
                    </td>
                    <td style={{ padding: '1rem' }}>
                      <span className={`status-pill status-${(a.status || 'APPLIED').toLowerCase()}`}>
                        {a.status}
                      </span>
                    </td>
                    <td style={{ padding: '1rem', color: 'var(--text-muted)', fontSize: '0.8rem' }}>
                      {new Date(a.appliedAt).toLocaleDateString()}
                    </td>
                    <td style={{ padding: '1rem', textAlign: 'right' }}>
                      <select
                        className="input-field"
                        style={{ height: '30px', fontSize: '0.75rem', width: '130px', display: 'inline-block' }}
                        value={a.status}
                        onChange={(e) => handleUpdateAppStatus(a.id, e.target.value)}>
                        <option value="APPLIED">APPLIED</option>
                        <option value="SHORTLISTED">SHORTLISTED</option>
                        <option value="INTERVIEW_SCHEDULED">INTERVIEW</option>
                        <option value="HIRED">HIRED</option>
                        <option value="REJECTED">REJECTED</option>
                      </select>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* ======================================================== */}
      {/* TAB 5: USER MANAGEMENT */}
      {/* ======================================================== */}
      {activeTab === 'users' && (
        <div className="glass-card" style={{ padding: '1.75rem' }}>
          <h2 style={{ fontSize: '1.35rem', marginBottom: '1.25rem' }}>Registered Users Directory</h2>
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
              <thead>
                <tr style={{ borderBottom: '1px solid var(--border-color)', color: 'var(--text-muted)', fontSize: '0.82rem' }}>
                  <th style={{ padding: '0.75rem 1rem' }}>ID</th>
                  <th style={{ padding: '0.75rem 1rem' }}>FULL NAME</th>
                  <th style={{ padding: '0.75rem 1rem' }}>EMAIL</th>
                  <th style={{ padding: '0.75rem 1rem' }}>ROLE</th>
                  <th style={{ padding: '0.75rem 1rem' }}>STATUS</th>
                  <th style={{ padding: '0.75rem 1rem', textAlign: 'right' }}>ACTION</th>
                </tr>
              </thead>
              <tbody>
                {users.map((u) => (
                  <tr key={u.id} style={{ borderBottom: '1px solid var(--border-color)' }}>
                    <td style={{ padding: '1rem', color: 'var(--text-muted)' }}>#{u.id}</td>
                    <td style={{ padding: '1rem', fontWeight: 700 }}>{u.fullName}</td>
                    <td style={{ padding: '1rem', color: 'var(--text-secondary)' }}>{u.email}</td>
                    <td style={{ padding: '1rem' }}>
                      <span style={{
                        background: u.role === 'ADMIN' ? 'rgba(239, 68, 68, 0.2)' : (u.role === 'RECRUITER' ? 'rgba(168, 85, 247, 0.2)' : 'rgba(59, 130, 246, 0.2)'),
                        color: u.role === 'ADMIN' ? '#f87171' : (u.role === 'RECRUITER' ? '#c084fc' : '#60a5fa'),
                        padding: '0.2rem 0.5rem',
                        borderRadius: '4px',
                        fontSize: '0.75rem',
                        fontWeight: 700
                      }}>
                        {u.role}
                      </span>
                    </td>
                    <td style={{ padding: '1rem' }}>
                      <span style={{ color: u.status === 'ACTIVE' ? '#34d399' : '#f87171', fontWeight: 600, fontSize: '0.85rem' }}>
                        {u.status}
                      </span>
                    </td>
                    <td style={{ padding: '1rem', textAlign: 'right' }}>
                      {u.role !== 'ADMIN' && (
                        <button
                          onClick={() => handleToggleUserStatus(u.id, u.status)}
                          className="btn-secondary btn-sm"
                          style={{ color: u.status === 'ACTIVE' ? '#f87171' : '#34d399', fontSize: '0.75rem', padding: '0.3rem 0.6rem' }}>
                          {u.status === 'ACTIVE' ? 'Suspend' : 'Activate'}
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
