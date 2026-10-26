import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import { Search, Sparkles, Building2, Briefcase, Award, ArrowRight, CheckCircle2 } from 'lucide-react';

const Home = () => {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchFeaturedJobs();
  }, []);

  const fetchFeaturedJobs = async () => {
    try {
      const res = await api.get('/jobs/search?size=6');
      const jobsList = Array.isArray(res.data) ? res.data : (res.data?.content || []);
      setJobs(jobsList);
    } catch (e) {
      console.error('Failed to load featured jobs', e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '1280px', margin: '0 auto', padding: '2rem 1.5rem' }}>
      {/* Hero Section */}
      <div className="glass-card" style={{
        padding: '4rem 2rem',
        textAlign: 'center',
        background: 'radial-gradient(circle at top, rgba(99, 102, 241, 0.15) 0%, rgba(17, 24, 39, 0.8) 70%)',
        marginBottom: '3rem',
        position: 'relative',
        overflow: 'hidden'
      }}>
        <div style={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: '0.5rem',
          padding: '0.4rem 1rem',
          borderRadius: 'var(--radius-full)',
          background: 'rgba(99, 102, 241, 0.15)',
          color: '#a855f7',
          border: '1px solid rgba(168, 85, 247, 0.3)',
          fontWeight: 600,
          fontSize: '0.85rem',
          marginBottom: '1.5rem'
        }}>
          <Sparkles size={16} /> Intelligent AI Resume Parser & Match Engine
        </div>

        <h1 style={{ fontSize: '3rem', lineHeight: 1.2, marginBottom: '1rem', maxWidth: '850px', margin: '0 auto 1.5rem auto' }}>
          Smart Hire <span style={{ background: 'var(--accent-gradient)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>AI-Powered</span> Job Portal & Candidate Screening
        </h1>

        <p style={{ color: 'var(--text-secondary)', fontSize: '1.15rem', maxWidth: '680px', margin: '0 auto 2rem auto' }}>
          Evaluate resumes using advanced NLP skill normalization, experience matching, and automated candidate ranking for SDE / Full Stack roles.
        </p>

        <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center' }}>
          <Link to="/jobs" className="btn-primary" style={{ padding: '0.85rem 2rem', fontSize: '1.05rem' }}>
            <Search size={20} /> Browse All Jobs
          </Link>
          <Link to="/register" className="btn-secondary" style={{ padding: '0.85rem 2rem', fontSize: '1.05rem' }}>
            Hire Candidates
          </Link>
        </div>
      </div>

      {/* Feature Highlights Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '1.5rem', marginBottom: '3.5rem' }}>
        <div className="glass-card" style={{ padding: '1.75rem' }}>
          <div style={{ background: 'rgba(99, 102, 241, 0.2)', width: '48px', height: '48px', borderRadius: '12px', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#6366f1', marginBottom: '1rem' }}>
            <Sparkles size={24} />
          </div>
          <h3 style={{ fontSize: '1.2rem', marginBottom: '0.5rem' }}>AI Resume Scoring (0–100)</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
            Extracts PDF/DOC text via Apache Tika and scores candidates across Skill overlap, Experience, Education, and JD relevance.
          </p>
        </div>

        <div className="glass-card" style={{ padding: '1.75rem' }}>
          <div style={{ background: 'rgba(16, 185, 129, 0.2)', width: '48px', height: '48px', borderRadius: '12px', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#10b981', marginBottom: '1rem' }}>
            <Award size={24} />
          </div>
          <h3 style={{ fontSize: '1.2rem', marginBottom: '0.5rem' }}>Candidate Ranking Dashboard</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
            Recruiters view candidates sorted by AI Match score (90+ Excellent, 80+ Strong Match) with detailed strengths and missing skills.
          </p>
        </div>

        <div className="glass-card" style={{ padding: '1.75rem' }}>
          <div style={{ background: 'rgba(168, 85, 247, 0.2)', width: '48px', height: '48px', borderRadius: '12px', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#a855f7', marginBottom: '1rem' }}>
            <Briefcase size={24} />
          </div>
          <h3 style={{ fontSize: '1.2rem', marginBottom: '0.5rem' }}>Application Management</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
            Track complete hiring workflow: Applied → Shortlisted → Interview Scheduled → Selected/Rejected with automated notifications.
          </p>
        </div>
      </div>

      {/* Featured Jobs Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <div>
          <h2 style={{ fontSize: '1.8rem' }}>Latest High-Tech Opportunities</h2>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.95rem' }}>Curated software engineering roles in Bengaluru, Gurugram, Hyderabad & Remote</p>
        </div>
        <Link to="/jobs" style={{ color: 'var(--accent-primary)', fontWeight: 600, display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
          View All <ArrowRight size={16} />
        </Link>
      </div>

      {/* Jobs Grid */}
      {loading ? (
        <div style={{ textAlign: 'center', padding: '3rem', color: 'var(--text-muted)' }}>Loading featured opportunities...</div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(340px, 1fr))', gap: '1.5rem' }}>
          {jobs.map((job) => (
            <div key={job.id} className="glass-card" style={{ padding: '1.5rem', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1rem' }}>
                  <div>
                    <h3 style={{ fontSize: '1.2rem', color: 'var(--text-primary)', marginBottom: '0.25rem' }}>{job.title}</h3>
                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
                      <Building2 size={14} /> {job.companyName} • {job.location}
                    </div>
                  </div>
                  <span style={{ background: 'rgba(255, 255, 255, 0.08)', padding: '0.25rem 0.6rem', borderRadius: 'var(--radius-sm)', fontSize: '0.75rem', fontWeight: 600 }}>
                    {job.jobType}
                  </span>
                </div>

                <p style={{ color: 'var(--text-muted)', fontSize: '0.88rem', lineClamp: 2, WebkitLineClamp: 2, display: '-webkit-box', WebkitBoxOrient: 'vertical', overflow: 'hidden', marginBottom: '1rem' }}>
                  {job.description}
                </p>

                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.4rem', marginBottom: '1.25rem' }}>
                  {job.requiredSkills.map((sk, idx) => (
                    <span key={idx} style={{ background: 'rgba(99, 102, 241, 0.1)', color: '#818cf8', border: '1px solid rgba(99, 102, 241, 0.2)', padding: '0.15rem 0.5rem', borderRadius: 'var(--radius-sm)', fontSize: '0.75rem' }}>
                      {sk}
                    </span>
                  ))}
                </div>
              </div>

              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: '1rem', borderTop: '1px solid var(--border-color)' }}>
                <div style={{ fontSize: '0.9rem', fontWeight: 700, color: 'var(--success-color)' }}>
                  ₹{(job.salaryMin / 100000).toFixed(1)}L - {(job.salaryMax / 100000).toFixed(1)}L LPA
                </div>
                <Link to={`/jobs/${job.id}`} className="btn-primary btn-sm">
                  Apply Now
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Home;
