import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Link, Navigate, Route, Routes, useLocation, useNavigate } from 'react-router-dom';
import { Briefcase, CalendarCheck, ClipboardList, Gauge, LogOut, Search, Settings as SettingsIcon, Users, WalletCards } from 'lucide-react';
import './styles.css';

const API_BASE = '/api';

async function api(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    credentials: 'include',
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options,
  });
  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `Request failed with status ${response.status}`);
  }
  return response.json();
}

function currency(value) {
  return Number(value || 0).toLocaleString('en-IN', { maximumFractionDigits: 2 });
}

const AuthContext = createContext(null);

function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api('/auth/me')
      .then((data) => setUser(data.authenticated ? data : null))
      .catch(() => setUser(null))
      .finally(() => setLoading(false));
  }, []);

  const value = useMemo(() => ({
    user,
    loading,
    login: async (email, password) => {
      const data = await api('/auth/login', { method: 'POST', body: JSON.stringify({ email, password }) });
      setUser({ authenticated: true, email: data.email });
    },
    signup: (fullName, email, password) => api('/auth/signup', { method: 'POST', body: JSON.stringify({ fullName, email, password }) }),
    logout: async () => {
      await api('/auth/logout', { method: 'POST' });
      setUser(null);
    },
  }), [user, loading]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

function useAuth() {
  return useContext(AuthContext);
}

function ProtectedRoute({ children }) {
  const { user, loading } = useAuth();
  if (loading) return <div className="loading">Loading HRMS...</div>;
  return user ? children : <Navigate to="/login" replace />;
}

function AuthPage({ mode }) {
  const auth = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ fullName: '', email: '', password: '' });
  const [error, setError] = useState('');
  const isSignup = mode === 'signup';

  async function submit(event) {
    event.preventDefault();
    setError('');
    try {
      if (isSignup) {
        await auth.signup(form.fullName, form.email, form.password);
      }
      await auth.login(form.email, form.password);
      navigate('/');
    } catch (err) {
      setError(err.message.includes('Failed to fetch')
        ? 'Server is not reachable. Please start the application and try again.'
        : 'Login failed. Check email/password or create a new account.');
    }
  }

  return (
    <main className="auth-shell">
      <section className="auth-visual">
        <div className="auth-brand"><span>HR</span><strong>Infotroz</strong></div>
        <h1>{isSignup ? 'Create a secure HR account for your company workspace.' : 'Manage people, attendance, leave, hiring, and payroll from one modern HRMS.'}</h1>
        <div className="auth-preview-grid">
          <article><span>Employees</span><strong>Directory</strong></article>
          <article><span>Attendance</span><strong>Live</strong></article>
          <article><span>Payroll</span><strong>Ready</strong></article>
        </div>
      </section>
      <section className="auth-card">
        <p className="eyebrow">Secure Access</p>
        <h2>{isSignup ? 'Create account' : 'Sign in'}</h2>
        <p className="muted">{isSignup ? 'Set up an HR admin account.' : 'Default: admin@hrms.com / admin123'}</p>
        {error && <p className="flash error">{error}</p>}
        <form className="form-grid" onSubmit={submit}>
          {isSignup && <label>Full Name<input required value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} /></label>}
          <label>Email<input type="email" required placeholder="admin@hrms.com" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} /></label>
          <label>Password<input type="password" required placeholder="admin123" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} /></label>
          <button className="primary-action" type="submit">{isSignup ? 'Create Account' : 'Sign In'}</button>
        </form>
        <p className="auth-switch">{isSignup ? 'Already have an account?' : 'New HR user?'} <Link to={isSignup ? '/login' : '/signup'}>{isSignup ? 'Sign in' : 'Create one'}</Link></p>
      </section>
    </main>
  );
}

function Layout({ children }) {
  const location = useLocation();
  const navigate = useNavigate();
  const auth = useAuth();
  const [settings, setSettings] = useState({ companyName: 'PeopleCore', hrManager: 'HR Manager' });
  const nav = [
    ['/', 'Dashboard', Gauge],
    ['/employees', 'Employees', Users],
    ['/attendance', 'Attendance', CalendarCheck],
    ['/payroll', 'Payroll', WalletCards],
    ['/leave', 'Leave', ClipboardList],
    ['/jobs', 'Hiring', Briefcase],
    ['/reports', 'Reports', ClipboardList],
    ['/settings', 'Settings', SettingsIcon],
  ];

  useEffect(() => {
    api('/settings').then(setSettings).catch(() => {});
  }, []);

  async function logout() {
    await auth.logout();
    navigate('/login');
  }

  function search(event) {
    event.preventDefault();
    const query = new FormData(event.currentTarget).get('search');
    navigate(`/employees?search=${encodeURIComponent(query || '')}`);
  }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand"><span>HR</span><div><strong>{settings.companyName}</strong><small>Management Suite</small></div></div>
        <nav>{nav.map(([to, label, Icon]) => <Link key={to} className={location.pathname === to ? 'active' : ''} to={to}><Icon size={18} />{label}</Link>)}</nav>
      </aside>
      <main className="main">
        <section className="topbar">
          <div><p className="eyebrow">Company HRMS</p><h1>{nav.find(([to]) => to === location.pathname)?.[1] || 'HRMS'}</h1></div>
          <form className="global-search" onSubmit={search}><Search size={18} /><input name="search" placeholder="Search employees, teams, roles" /></form>
          <div className="profile-pill"><span>HM</span><div><strong>{settings.hrManager}</strong><small>{auth.user?.email}</small></div><button className="logout-button" onClick={logout}><LogOut size={16} />Logout</button></div>
        </section>
        {children}
      </main>
    </div>
  );
}

function Metric({ label, value, to }) {
  return <Link className="metric-card" to={to}><span>{label}</span><strong>{value}</strong></Link>;
}

function Dashboard() {
  const [data, setData] = useState(null);
  const load = () => api('/dashboard').then(setData);
  useEffect(() => { load(); }, []);
  if (!data) return <div className="loading">Loading dashboard...</div>;

  async function decideLeave(id, action) {
    await api(`/leave/${id}/${action}`, { method: 'POST' });
    load();
  }

  return (
    <>
      <section className="metric-grid">
        <Metric label="Total Employees" value={data.employeeCount} to="/employees" />
        <Metric label="Present Today" value={data.presentToday} to="/attendance" />
        <Metric label="Open Positions" value={data.openPositions} to="/jobs" />
        <Metric label="Leave Requests" value={data.pendingLeaves} to="/leave" />
        <Metric label="Monthly Payroll" value={currency(data.totalPayroll)} to="/payroll" />
      </section>
      <section className="dashboard-grid">
        <article className="panel dashboard-main">
          <div className="panel-header"><div><h2>Employee Directory</h2><p>Live workforce list with role, department, status, and salary.</p></div><Link className="secondary-action" to="/employees">Open Directory</Link></div>
          <Table headers={['Employee', 'Department', 'Role', 'Status', 'Salary']} rows={data.employees.map((e) => [<NameCell item={e} />, e.department, e.role, <Status value={e.status} />, currency(e.monthlySalary)])} />
        </article>
        <aside className="side-stack">
          <Panel title="Attendance Summary" subtitle={`${data.presentToday} present, ${data.absentToday} absent today`}>
            <div className="attendance-bars"><div><span>Present</span><strong>{data.presentToday}</strong></div><div><span>Absent</span><strong>{data.absentToday}</strong></div><div><span>Pending Leave</span><strong>{data.pendingLeaves}</strong></div></div>
          </Panel>
          <Panel title="Leave Requests" subtitle={`${data.pendingLeaves} pending approval`}>
            <div className="mini-list action-list">
              {data.leaveRequests.length === 0 && <p className="empty-state">No pending leave requests.</p>}
              {data.leaveRequests.map((r) => <div key={r.id}><section><strong>{r.employeeName}</strong><span>{r.leaveType} | {r.startDate} to {r.endDate}</span></section><button onClick={() => decideLeave(r.id, 'approve')}>Approve</button><button className="danger" onClick={() => decideLeave(r.id, 'reject')}>Reject</button></div>)}
            </div>
          </Panel>
          <Panel title="Open Hiring" subtitle={`${data.openPositions} positions open`}>
            <div className="mini-list">{data.jobOpenings.filter((j) => j.status === 'Open').map((j) => <div key={j.id}><strong>{j.title}</strong><span>{j.department} - {j.openings}</span></div>)}</div>
          </Panel>
        </aside>
      </section>
    </>
  );
}

function Employees() {
  const [employees, setEmployees] = useState([]);
  const [form, setForm] = useState({ name: '', email: '', department: '', role: '', monthlySalary: '', joiningDate: '', birthDate: '' });
  const search = new URLSearchParams(useLocation().search).get('search') || '';
  const load = () => api(`/employees?search=${encodeURIComponent(search)}`).then(setEmployees);
  useEffect(() => { load(); }, [search]);

  async function add(event) {
    event.preventDefault();
    await api('/employees', { method: 'POST', body: JSON.stringify({ ...form, monthlySalary: Number(form.monthlySalary) }) });
    setForm({ name: '', email: '', department: '', role: '', monthlySalary: '', joiningDate: '', birthDate: '' });
    load();
  }

  async function update(employee) {
    await api(`/employees/${employee.id}`, { method: 'PUT', body: JSON.stringify(employee) });
    load();
  }

  async function remove(id) {
    await api(`/employees/${id}`, { method: 'DELETE' });
    load();
  }

  return (
    <section className="split-layout">
      <Panel title="Add Employee" subtitle="Create a new employee profile.">
        <form className="form-grid" onSubmit={add}>{['name', 'email', 'department', 'role', 'monthlySalary', 'joiningDate', 'birthDate'].map((key) => <label key={key}>{labelize(key)}<input required type={dateField(key) ? 'date' : key === 'monthlySalary' ? 'number' : key === 'email' ? 'email' : 'text'} value={form[key]} onChange={(e) => setForm({ ...form, [key]: e.target.value })} /></label>)}<button>Add Employee</button></form>
      </Panel>
      <Panel title="Team List" subtitle="Update roles, departments, salaries, and status.">
        <div className="employee-list">{employees.map((e) => <EmployeeRow key={e.id} employee={e} onSave={update} onDelete={remove} />)}</div>
      </Panel>
    </section>
  );
}

function EmployeeRow({ employee, onSave, onDelete }) {
  const [draft, setDraft] = useState(employee);
  return <div className="employee-row"><NameCell item={employee} /><div className="inline-edit"><input value={draft.department} onChange={(e) => setDraft({ ...draft, department: e.target.value })} /><input value={draft.role} onChange={(e) => setDraft({ ...draft, role: e.target.value })} /><input type="number" value={draft.monthlySalary} onChange={(e) => setDraft({ ...draft, monthlySalary: Number(e.target.value) })} /><select value={draft.status} onChange={(e) => setDraft({ ...draft, status: e.target.value })}><option>Active</option><option>Absent</option><option>On Leave</option><option>Inactive</option></select><button onClick={() => onSave(draft)}>Save</button></div><button className="danger" onClick={() => onDelete(employee.id)}>Delete</button></div>;
}

function Attendance() {
  const [data, setData] = useState(null);
  const [form, setForm] = useState({ employeeId: '', date: '', present: true });
  const load = () => api('/attendance').then((d) => { setData(d); setForm((f) => ({ ...f, date: d.today, employeeId: f.employeeId || d.employees[0]?.id || '' })); });
  useEffect(() => { load(); }, []);
  if (!data) return <div className="loading">Loading attendance...</div>;
  async function submit(event) { event.preventDefault(); await api('/attendance', { method: 'POST', body: JSON.stringify({ ...form, employeeId: Number(form.employeeId), present: form.present === true || form.present === 'true' }) }); load(); }
  return <><section className="metric-grid compact-metrics"><Metric label="Present Today" value={data.presentToday} to="/attendance" /><Metric label="Absent Today" value={data.absentToday} to="/attendance" /><Metric label="Total Employees" value={data.employees.length} to="/employees" /></section><Panel title="Mark Attendance" subtitle="Record presence for a selected employee and date."><form className="form-grid" onSubmit={submit}><label>Employee<select value={form.employeeId} onChange={(e) => setForm({ ...form, employeeId: e.target.value })}>{data.employees.map((e) => <option key={e.id} value={e.id}>{e.name} - {e.department}</option>)}</select></label><label>Date<input type="date" value={form.date} onChange={(e) => setForm({ ...form, date: e.target.value })} /></label><label>Status<select value={form.present} onChange={(e) => setForm({ ...form, present: e.target.value })}><option value="true">Present</option><option value="false">Absent</option></select></label><button>Save Attendance</button></form></Panel></>;
}

function Leave() {
  const [data, setData] = useState(null);
  const [form, setForm] = useState({ employeeId: '', leaveType: 'Casual Leave', startDate: '', endDate: '', reason: '' });
  const load = () => api('/leave').then((d) => { setData(d); setForm((f) => ({ ...f, employeeId: f.employeeId || d.employees[0]?.id || '' })); });
  useEffect(() => { load(); }, []);
  if (!data) return <div className="loading">Loading leave...</div>;
  async function submit(event) { event.preventDefault(); await api('/leave', { method: 'POST', body: JSON.stringify({ ...form, employeeId: Number(form.employeeId) }) }); setForm({ ...form, reason: '' }); load(); }
  async function decide(id, action) { await api(`/leave/${id}/${action}`, { method: 'POST' }); load(); }
  return <section className="split-layout"><Panel title="New Leave Request" subtitle="Submit leave for approval."><form className="form-grid" onSubmit={submit}><label>Employee<select value={form.employeeId} onChange={(e) => setForm({ ...form, employeeId: e.target.value })}>{data.employees.map((e) => <option key={e.id} value={e.id}>{e.name}</option>)}</select></label><label>Leave Type<select value={form.leaveType} onChange={(e) => setForm({ ...form, leaveType: e.target.value })}><option>Casual Leave</option><option>Medical Leave</option><option>Earned Leave</option><option>Work From Home</option></select></label><label>Start Date<input type="date" value={form.startDate} onChange={(e) => setForm({ ...form, startDate: e.target.value })} required /></label><label>End Date<input type="date" value={form.endDate} onChange={(e) => setForm({ ...form, endDate: e.target.value })} required /></label><label>Reason<input value={form.reason} onChange={(e) => setForm({ ...form, reason: e.target.value })} required /></label><button>Submit Request</button></form></Panel><Panel title="Leave Requests" subtitle="Approve or reject pending requests."><div className="request-list">{data.leaveRequests.map((r) => <div className="request-row" key={r.id}><div><strong>{r.employeeName}</strong><span>{r.leaveType} | {r.startDate} to {r.endDate}</span><small>{r.reason}</small></div><Status value={r.status} />{r.status === 'Pending' && <button onClick={() => decide(r.id, 'approve')}>Approve</button>}{r.status === 'Pending' && <button className="danger" onClick={() => decide(r.id, 'reject')}>Reject</button>}</div>)}</div></Panel></section>;
}

function Jobs() {
  const [jobs, setJobs] = useState([]);
  const [form, setForm] = useState({ title: '', department: '', location: '', openings: 1 });
  const load = () => api('/jobs').then(setJobs);
  useEffect(() => { load(); }, []);
  async function add(event) { event.preventDefault(); await api('/jobs', { method: 'POST', body: JSON.stringify({ ...form, openings: Number(form.openings) }) }); setForm({ title: '', department: '', location: '', openings: 1 }); load(); }
  async function close(id) { await api(`/jobs/${id}/close`, { method: 'POST' }); load(); }
  return <section className="split-layout"><Panel title="Create Opening" subtitle="Add a new position."><form className="form-grid" onSubmit={add}>{['title', 'department', 'location', 'openings'].map((k) => <label key={k}>{labelize(k)}<input required type={k === 'openings' ? 'number' : 'text'} value={form[k]} onChange={(e) => setForm({ ...form, [k]: e.target.value })} /></label>)}<button>Create Opening</button></form></Panel><Panel title="Open Positions" subtitle="Track recruitment demand."><Table headers={['Role', 'Department', 'Location', 'Openings', 'Status', 'Action']} rows={jobs.map((j) => [j.title, j.department, j.location, j.openings, <Status value={j.status} />, j.status === 'Open' ? <button className="secondary-button" onClick={() => close(j.id)}>Close</button> : ''])} /></Panel></section>;
}

function Payroll() {
  const [data, setData] = useState(null);
  const [form, setForm] = useState({ employeeId: '', month: new Date().toISOString().slice(0, 7) });
  const load = () => api(`/payroll?${form.employeeId ? `employeeId=${form.employeeId}&` : ''}month=${form.month}`).then(setData);
  useEffect(() => { api('/payroll').then((d) => { setData(d); setForm((f) => ({ ...f, employeeId: d.employees[0]?.id || '' })); }); }, []);
  if (!data) return <div className="loading">Loading payroll...</div>;
  return <section className="split-layout"><Panel title="Generate Payroll" subtitle="Select employee and month."><form className="form-grid" onSubmit={(e) => { e.preventDefault(); load(); }}><label>Employee<select value={form.employeeId} onChange={(e) => setForm({ ...form, employeeId: e.target.value })}>{data.employees.map((e) => <option key={e.id} value={e.id}>{e.name} - {e.role}</option>)}</select></label><label>Month<input type="month" value={form.month} onChange={(e) => setForm({ ...form, month: e.target.value })} /></label><button>Generate</button></form></Panel>{data.summary && typeof data.summary === 'object' && <Panel title={data.summary.employee.name} subtitle={data.summary.month}><dl className="payroll-grid"><div><dt>Working Days</dt><dd>{data.summary.workingDays}</dd></div><div><dt>Present Days</dt><dd>{data.summary.presentDays}</dd></div><div><dt>Gross Salary</dt><dd>{currency(data.summary.grossSalary)}</dd></div><div><dt>Deductions</dt><dd>{currency(data.summary.deductions)}</dd></div><div className="net"><dt>Net Salary</dt><dd>{currency(data.summary.netSalary)}</dd></div></dl></Panel>}</section>;
}

function Reports() {
  const [data, setData] = useState(null);
  useEffect(() => { api('/reports').then(setData); }, []);
  if (!data) return <div className="loading">Loading reports...</div>;
  return <><section className="metric-grid compact-metrics"><Metric label="Employees" value={data.employeeCount} to="/employees" /><Metric label="Active" value={data.activeEmployees} to="/reports" /><Metric label="On Leave" value={data.onLeaveEmployees} to="/leave" /></section><Panel title="Workforce Report" subtitle="Department-wise employee and salary view."><button onClick={() => window.print()}>Print Report</button><Table headers={['Name', 'Department', 'Role', 'Status', 'Salary']} rows={data.employees.map((e) => [e.name, e.department, e.role, e.status, currency(e.monthlySalary)])} /></Panel></>;
}

function Settings() {
  const [settings, setSettings] = useState(null);
  useEffect(() => { api('/settings').then(setSettings); }, []);
  if (!settings) return <div className="loading">Loading settings...</div>;
  async function save(event) { event.preventDefault(); setSettings(await api('/settings', { method: 'PUT', body: JSON.stringify(settings) })); }
  return <Panel title="Company Settings" subtitle="Update company profile used across HRMS."><form className="form-grid narrow-form" onSubmit={save}>{['companyName', 'hrManager', 'officeLocation', 'annualLeaveDays'].map((k) => <label key={k}>{labelize(k)}<input type={k === 'annualLeaveDays' ? 'number' : 'text'} value={settings[k]} onChange={(e) => setSettings({ ...settings, [k]: k === 'annualLeaveDays' ? Number(e.target.value) : e.target.value })} /></label>)}<button>Save Settings</button></form></Panel>;
}

function Panel({ title, subtitle, children }) {
  return <article className="panel"><div className="panel-header"><div><h2>{title}</h2>{subtitle && <p>{subtitle}</p>}</div></div>{children}</article>;
}

function Table({ headers, rows }) {
  return <div className="table-wrap"><table><thead><tr>{headers.map((h) => <th key={h}>{h}</th>)}</tr></thead><tbody>{rows.map((row, i) => <tr key={i}>{row.map((cell, j) => <td key={j}>{cell}</td>)}</tr>)}</tbody></table></div>;
}

function NameCell({ item }) {
  return <div className="employee-summary"><strong>{item.name}</strong><span>{item.email}</span></div>;
}

function Status({ value }) {
  const tone = value === 'Active' || value === 'Approved' || value === 'Open' ? 'good' : value === 'Rejected' || value === 'Closed' || value === 'Absent' ? 'bad' : 'warn';
  return <span className={`status ${tone}`}>{value}</span>;
}

function labelize(value) {
  return value.replace(/([A-Z])/g, ' $1').replace(/^./, (c) => c.toUpperCase());
}

function dateField(key) {
  return key.toLowerCase().includes('date');
}

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<AuthPage mode="login" />} />
          <Route path="/signup" element={<AuthPage mode="signup" />} />
          <Route path="/*" element={<ProtectedRoute><Layout><Routes><Route path="/" element={<Dashboard />} /><Route path="/employees" element={<Employees />} /><Route path="/attendance" element={<Attendance />} /><Route path="/payroll" element={<Payroll />} /><Route path="/leave" element={<Leave />} /><Route path="/jobs" element={<Jobs />} /><Route path="/reports" element={<Reports />} /><Route path="/settings" element={<Settings />} /></Routes></Layout></ProtectedRoute>} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

createRoot(document.getElementById('root')).render(<App />);
