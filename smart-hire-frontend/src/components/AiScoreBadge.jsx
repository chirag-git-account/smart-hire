import React from 'react';
import { Sparkles } from 'lucide-react';

const AiScoreBadge = ({ score, recommendation }) => {
  if (score === null || score === undefined) {
    return <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>Analyzing...</span>;
  }

  let badgeClass = 'score-low';
  let label = 'Low Match';

  if (score >= 90) {
    badgeClass = 'score-excellent';
    label = 'Excellent Match';
  } else if (score >= 80) {
    badgeClass = 'score-strong';
    label = 'Strong Match';
  } else if (score >= 70) {
    badgeClass = 'score-good';
    label = 'Good Match';
  } else if (score >= 60) {
    badgeClass = 'score-moderate';
    label = 'Moderate Match';
  }

  return (
    <span className={`badge-score ${badgeClass}`}>
      <Sparkles size={14} />
      <span>{score}/100</span>
      <span style={{ opacity: 0.85, fontSize: '0.75rem', marginLeft: '0.25rem' }}>({label})</span>
    </span>
  );
};

export default AiScoreBadge;
