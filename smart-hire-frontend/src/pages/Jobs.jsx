import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import { Search, MapPin, Briefcase, DollarSign, Filter, ChevronLeft, ChevronRight } from 'lucide-react';

const Jobs = () => {
  const [jobs, setJobs] = useState([]);
  const [keyword, setKeyword] = useState('');
  const [location, setLocation] = useState('');
  const [jobType, setJobType] = useState('');
  const [expYears, setExpYears] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchJobs();
  }, [page, jobType]);

  const fetchJobs = async () => {
    setLoading(true);
    try {
      let query = `/jobs/search?page=${page}&size=6`;
      if (keyword) query += `&keyword=${encodeURIComponent(keyword)}`;
      if (location) query += `&location=${encodeURIComponent(location)}`;
      if (jobType) query += `&jobType=${jobType}`;
      if (expYears) query += `&expYears=${expYears}`;

      const res = await api.get(query);
      const jobsList = Array.isArray(res.data) ? res.data : (res.data?.content || []);
      setJobs(jobsList);
      setTotalPages(res.data?.totalPages || (jobsList.length > 0 ? 1 : 0));
    } catch (e) {
      console.error('Failed to fetch jobs', e);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0);
    fetchJobs();
  };

  return (
    <div style={{ maxWidth: '1280px', margin: '0 auto', padding: '2rem 1.5rem' }}>
      <div style={{ marginBottom: '2rem' }}>
        <h1 style={{ fontSize: '2.2rem', marginBottom: '0.5rem' }}>Explore Software Jobs</h1>
        <p style={{ color: 'var(--text-secondary)' }}>Discover high-growth engineering roles matched to your technical skills.</p>
      </div>

      {/* Filter Bar */}
      <form onSubmit={handleSearch} className="glass-card" style={{ padding: '1.5rem', marginBottom: '2rem', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem', alignItems: 'end' }}>
        <div>
          <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Keyword / Title</label>
          <input type="text" className="input-field" placeholder="e.g. Java, React, Microservices" value={keyword} onChange={(e) => setKeyword(e.target.value)} />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Location</label>
          <input type="text" className="input-field" placeholder="e.g. Bengaluru, Remote" value={location} onChange={(e) => setLocation(e.target.value)} />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Job Type</label>
          <select className="input-field" value={jobType} onChange={(e) => setJobType(e.target.value)}>
            <option value="">All Types</option>
            <option value="FULL_TIME">Full Time</option>
            <option value="REMOTE">Remote</option>
            <option value="HYBRID">Hybrid</option>
            <option value="CONTRACT">Contract</option>
          </select>
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Min Experience (Years)</label>
          <input type="number" className="input-field" placeholder="e.g. 3" value={expYears} onChange={(e) => setExpYears(e.target.value)} />
        </div>

        <button type="submit" className="btn-primary" style={{ width: '100%' }}>
          <Search size={18} /> Search Jobs
        </button>
      </form>

      {/* Jobs Grid */}
      {loading ? (
        <div style={{ textAlign: 'center', padding: '4rem', color: 'var(--text-muted)' }}>Searching jobs...</div>
      ) : jobs.length === 0 ? (
        <div className="glass-card" style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
          No jobs found matching your criteria. Try adjusting keywords or filters.
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(350px, 1fr))', gap: '1.5rem', marginBottom: '2.5rem' }}>
          {jobs.map((job) => (
            <div key={job.id} className="glass-card" style={{ padding: '1.5rem', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '0.75rem' }}>
                  <div>
                    <h3 style={{ fontSize: '1.25rem', marginBottom: '0.2rem' }}>{job.title}</h3>
                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
                      <Briefcase size={14} /> {job.companyName}
                    </div>
                  </div>
                  {job.matchPercentage && (
                    <span style={{ background: 'rgba(16, 185, 129, 0.15)', color: '#34d399', border: '1px solid rgba(16, 185, 129, 0.3)', padding: '0.2rem 0.5rem', borderRadius: 'var(--radius-full)', fontSize: '0.75rem', fontWeight: 700 }}>
                      {job.matchPercentage}% Skill Match
                    </span>
                  )}
                </div>

                <div style={{ display: 'flex', gap: '1rem', color: 'var(--text-muted)', fontSize: '0.85rem', marginBottom: '1rem' }}>
                  <span style={{ display: 'flex', alignItems: 'center', gap: '0.25rem' }}><MapPin size={14} /> {job.location}</span>
                  <span style={{ display: 'flex', alignItems: 'center', gap: '0.25rem' }}><Briefcase size={14} /> {job.jobType}</span>
                </div>

                <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', marginBottom: '1.25rem', lineClamp: 3, WebkitLineClamp: 3, display: '-webkit-box', WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                  {job.description}
                </p>

                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.35rem', marginBottom: '1.25rem' }}>
                  {job.requiredSkills.map((s, idx) => (
                    <span key={idx} style={{ background: 'rgba(255, 255, 255, 0.05)', color: 'var(--text-primary)', border: '1px solid var(--border-color)', padding: '0.15rem 0.5rem', borderRadius: 'var(--radius-sm)', fontSize: '0.75rem' }}>
                      {s}
                    </span>
                  ))}
                </div>
              </div>

              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: '1rem', borderTop: '1px solid var(--border-color)' }}>
                <div style={{ color: 'var(--success-color)', fontWeight: 700, fontSize: '0.95rem' }}>
                  ₹{(job.salaryMin / 100000).toFixed(1)}L - {(job.salaryMax / 100000).toFixed(1)}L LPA
                </div>
                <Link to={`/jobs/${job.id}`} className="btn-primary btn-sm">
                  View Job & Apply
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Pagination Controls */}
      {totalPages > 1 && (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '1rem' }}>
          <button className="btn-secondary btn-sm" disabled={page === 0} onClick={() => setPage(page - 1)}>
            <ChevronLeft size={16} /> Previous
          </button>
          <span style={{ fontSize: '0.9rem', color: 'var(--text-secondary)' }}>Page {page + 1} of {totalPages}</span>
          <button className="btn-secondary btn-sm" disabled={page + 1 >= totalPages} onClick={() => setPage(page + 1)}>
            Next <ChevronRight size={16} />
          </button>
        </div>
      )}
    </div>
  );
};

export default Jobs;
