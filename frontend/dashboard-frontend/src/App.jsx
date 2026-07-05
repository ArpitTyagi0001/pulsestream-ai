import { useState, useEffect, useRef, useCallback } from "react";

const GATEWAY_URL = "http://localhost:8080";
const AUTH_URL = `${GATEWAY_URL}/auth-service`;
const QUERY_URL = `${GATEWAY_URL}/api/dashboard`;
const AI_URL = `${GATEWAY_URL}/api/ai`;

function PulseDot({ status = "live" }) {
  const color =
    status === "live" ? "#00FF94" : status === "warn" ? "#FFB020" : "#FF4D4D";
  return (
    <span style={{ position: "relative", display: "inline-flex", width: 10, height: 10 }}>
      <span
        style={{
          position: "absolute",
          inset: 0,
          borderRadius: "50%",
          background: color,
          opacity: 0.5,
          animation: "pulseRing 1.6s cubic-bezier(0.4,0,0.6,1) infinite",
        }}
      />
      <span
        style={{
          position: "relative",
          width: 10,
          height: 10,
          borderRadius: "50%",
          background: color,
          boxShadow: `0 0 8px ${color}`,
        }}
      />
    </span>
  );
}

function StatCard({ label, value, sub, mono }) {
  return (
    <div
      style={{
        background: "#11161A",
        border: "1px solid #1E2629",
        borderRadius: 4,
        padding: "20px 22px",
        flex: 1,
        minWidth: 160,
      }}
    >
      <div
        style={{
          fontSize: 11,
          letterSpacing: "0.08em",
          textTransform: "uppercase",
          color: "#6B7780",
          fontFamily: "Inter, sans-serif",
          marginBottom: 10,
        }}
      >
        {label}
      </div>
      <div
        style={{
          fontFamily: "'JetBrains Mono', monospace",
          fontSize: 32,
          fontWeight: 600,
          color: "#EDF2F4",
          lineHeight: 1,
        }}
      >
        {value}
      </div>
      {sub && (
        <div style={{ fontSize: 12, color: "#4D9B72", marginTop: 8, fontFamily: "'JetBrains Mono', monospace" }}>
          {sub}
        </div>
      )}
    </div>
  );
}

export default function Dashboard() {
  const [token, setToken] = useState(null);
  const [username, setUsername] = useState("");
  const [authMode, setAuthMode] = useState("login"); // login | register
  const [form, setForm] = useState({ username: "", password: "" });
  const [authError, setAuthError] = useState("");
  const [authLoading, setAuthLoading] = useState(false);

  const [totalEvents, setTotalEvents] = useState(null);
  const [latestEvents, setLatestEvents] = useState([]);
  const [locationCount, setLocationCount] = useState({});
  const [lastSync, setLastSync] = useState(null);
  const [dataError, setDataError] = useState("");
  const [connStatus, setConnStatus] = useState("live");

  const [summary, setSummary] = useState("");
  const [summaryLoading, setSummaryLoading] = useState(false);
  const [summaryError, setSummaryError] = useState("");

  const pollRef = useRef(null);

  // ---------- auth ----------
  async function handleAuthSubmit(e) {
    e.preventDefault();
    setAuthError("");
    setAuthLoading(true);
    try {
      if (authMode === "register") {
        const res = await fetch(`${AUTH_URL}/register`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(form),
        });
        if (!res.ok) throw new Error("Registration failed. Try a different username.");
        setAuthMode("login");
        setAuthError("Account created. Sign in below.");
        setAuthLoading(false);
        return;
      }

      const res = await fetch(`${AUTH_URL}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });
      const text = await res.text();
      if (!res.ok || text === "fail" || !text || text.length < 10) {
        throw new Error("Invalid username or password.");
      }
      setToken(text);
      setUsername(form.username);
    } catch (err) {
      setAuthError(err.message || "Something went wrong.");
    } finally {
      setAuthLoading(false);
    }
  }

  function handleLogout() {
    setToken(null);
    setUsername("");
    setForm({ username: "", password: "" });
    setTotalEvents(null);
    setLatestEvents([]);
    setLocationCount({});
    setSummary("");
    if (pollRef.current) clearInterval(pollRef.current);
  }

  // ---------- dashboard data ----------
  const fetchDashboard = useCallback(async () => {
    if (!token) return;
    try {
      const headers = { Authorization: `Bearer ${token}` };
      const [totalRes, latestRes, locRes] = await Promise.all([
        fetch(`${QUERY_URL}/total-events`, { headers }),
        fetch(`${QUERY_URL}/latest-events`, { headers }),
        fetch(`${QUERY_URL}/location-count`, { headers }),
      ]);

      if (totalRes.status === 401 || totalRes.status === 403) {
        throw new Error("Session expired. Please sign in again.");
      }

      const total = await totalRes.json();
      const latest = await latestRes.json();
      const locs = await locRes.json();

      setTotalEvents(total);
      setLatestEvents(Array.isArray(latest) ? latest.slice(0, 8) : []);
      setLocationCount(locs || {});
      setLastSync(new Date());
      setDataError("");
      setConnStatus("live");
    } catch (err) {
      setDataError(err.message || "Could not reach the query service.");
      setConnStatus("error");
      if (err.message.includes("Session expired")) handleLogout();
    }
  }, [token]);

  useEffect(() => {
    if (!token) return;
    fetchDashboard();
    pollRef.current = setInterval(fetchDashboard, 6000);
    return () => clearInterval(pollRef.current);
  }, [token, fetchDashboard]);

  // ---------- AI summary ----------
  async function handleGenerateSummary() {
    setSummaryLoading(true);
    setSummaryError("");
    setSummary("");
    try {
      const res = await fetch(`${AI_URL}/event-summary`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("AI service could not generate a summary right now.");
      const text = await res.text();
      setSummary(text);
    } catch (err) {
      setSummaryError(err.message || "Something went wrong generating the summary.");
    } finally {
      setSummaryLoading(false);
    }
  }

  const fontImport = (
    <style>{`
      @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=JetBrains+Mono:wght@400;500;600;700&display=swap');
      @keyframes pulseRing {
        0% { transform: scale(1); opacity: 0.5; }
        70% { transform: scale(2.2); opacity: 0; }
        100% { transform: scale(2.2); opacity: 0; }
      }
      @keyframes fadeUp {
        from { opacity: 0; transform: translateY(6px); }
        to { opacity: 1; transform: translateY(0); }
      }
      * { box-sizing: border-box; }
      input::placeholder { color: #4D5760; }
      input:focus, button:focus-visible { outline: 2px solid #00FF94; outline-offset: 2px; }
    `}</style>
  );

  // ---------- LOGIN / REGISTER SCREEN ----------
  if (!token) {
    return (
      <div
        style={{
          minHeight: "100vh",
          background: "#0A0E0F",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          fontFamily: "Inter, sans-serif",
          padding: 20,
        }}
      >
        {fontImport}
        <div
          style={{
            width: 380,
            background: "#11161A",
            border: "1px solid #1E2629",
            borderRadius: 6,
            padding: 36,
            animation: "fadeUp 0.4s ease",
          }}
        >
          <div style={{ display: "flex", alignItems: "center", gap: 8, marginBottom: 4 }}>
            <PulseDot status="live" />
            <span
              style={{
                fontFamily: "'JetBrains Mono', monospace",
                fontSize: 11,
                letterSpacing: "0.1em",
                color: "#4D9B72",
                textTransform: "uppercase",
              }}
            >
              stream-analytics
            </span>
          </div>
          <h1
            style={{
              fontFamily: "'JetBrains Mono', monospace",
              fontSize: 22,
              color: "#EDF2F4",
              margin: "14px 0 6px",
              fontWeight: 600,
            }}
          >
            {authMode === "login" ? "Sign in to console" : "Create an account"}
          </h1>
          <p style={{ color: "#6B7780", fontSize: 13, margin: "0 0 26px", lineHeight: 1.5 }}>
            {authMode === "login"
              ? "Authenticate to access the live event pipeline."
              : "Register a new operator account for the console."}
          </p>

          <form onSubmit={handleAuthSubmit}>
            <label
              style={{
                display: "block",
                fontSize: 11,
                color: "#6B7780",
                marginBottom: 6,
                letterSpacing: "0.04em",
              }}
            >
              USERNAME
            </label>
            <input
              type="text"
              required
              value={form.username}
              onChange={(e) => setForm({ ...form, username: e.target.value })}
              placeholder="operator_01"
              style={{
                width: "100%",
                padding: "11px 12px",
                background: "#0A0E0F",
                border: "1px solid #1E2629",
                borderRadius: 4,
                color: "#EDF2F4",
                fontFamily: "'JetBrains Mono', monospace",
                fontSize: 14,
                marginBottom: 16,
              }}
            />
            <label
              style={{
                display: "block",
                fontSize: 11,
                color: "#6B7780",
                marginBottom: 6,
                letterSpacing: "0.04em",
              }}
            >
              PASSWORD
            </label>
            <input
              type="password"
              required
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
              placeholder="••••••••"
              style={{
                width: "100%",
                padding: "11px 12px",
                background: "#0A0E0F",
                border: "1px solid #1E2629",
                borderRadius: 4,
                color: "#EDF2F4",
                fontFamily: "'JetBrains Mono', monospace",
                fontSize: 14,
                marginBottom: 20,
              }}
            />

            {authError && (
              <div
                style={{
                  fontSize: 12,
                  color: authError.includes("created") ? "#4D9B72" : "#FF8080",
                  marginBottom: 16,
                  fontFamily: "'JetBrains Mono', monospace",
                }}
              >
                {authError.includes("created") ? "✓ " : "✕ "}
                {authError}
              </div>
            )}

            <button
              type="submit"
              disabled={authLoading}
              style={{
                width: "100%",
                padding: "12px",
                background: authLoading ? "#1E2629" : "#00FF94",
                color: authLoading ? "#6B7780" : "#0A0E0F",
                border: "none",
                borderRadius: 4,
                fontFamily: "'JetBrains Mono', monospace",
                fontSize: 13,
                fontWeight: 600,
                letterSpacing: "0.04em",
                cursor: authLoading ? "default" : "pointer",
                transition: "opacity 0.15s",
              }}
            >
              {authLoading ? "WORKING..." : authMode === "login" ? "SIGN IN →" : "CREATE ACCOUNT →"}
            </button>
          </form>

          <div style={{ textAlign: "center", marginTop: 20 }}>
            <button
              onClick={() => {
                setAuthMode(authMode === "login" ? "register" : "login");
                setAuthError("");
              }}
              style={{
                background: "none",
                border: "none",
                color: "#6B7780",
                fontSize: 12,
                cursor: "pointer",
                fontFamily: "Inter, sans-serif",
              }}
            >
              {authMode === "login"
                ? "No account? Register here"
                : "Already registered? Sign in"}
            </button>
          </div>
        </div>
      </div>
    );
  }

  // ---------- MAIN DASHBOARD ----------
  const topLocations = Object.entries(locationCount).sort((a, b) => b[1] - a[1]).slice(0, 6);
  const maxLocCount = topLocations.length ? topLocations[0][1] : 1;

  return (
    <div style={{ minHeight: "100vh", background: "#0A0E0F", fontFamily: "Inter, sans-serif", color: "#EDF2F4" }}>
      {fontImport}

      {/* header */}
      <div
        style={{
          borderBottom: "1px solid #1E2629",
          padding: "16px 28px",
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          flexWrap: "wrap",
          gap: 12,
        }}
      >
        <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
          <PulseDot status={connStatus} />
          <span style={{ fontFamily: "'JetBrains Mono', monospace", fontSize: 13, letterSpacing: "0.05em" }}>
            STREAM-ANALYTICS <span style={{ color: "#6B7780" }}>// console</span>
          </span>
        </div>
        <div style={{ display: "flex", alignItems: "center", gap: 16 }}>
          <span style={{ fontSize: 12, color: "#6B7780", fontFamily: "'JetBrains Mono', monospace" }}>
            {username}
          </span>
          <button
            onClick={handleLogout}
            style={{
              background: "none",
              border: "1px solid #1E2629",
              color: "#6B7780",
              fontSize: 11,
              padding: "6px 12px",
              borderRadius: 4,
              cursor: "pointer",
              fontFamily: "'JetBrains Mono', monospace",
              letterSpacing: "0.04em",
            }}
          >
            SIGN OUT
          </button>
        </div>
      </div>

      <div style={{ padding: "28px", maxWidth: 1100, margin: "0 auto" }}>
        {dataError && (
          <div
            style={{
              background: "rgba(255,77,77,0.08)",
              border: "1px solid rgba(255,77,77,0.3)",
              borderRadius: 4,
              padding: "10px 14px",
              fontSize: 12,
              color: "#FF8080",
              marginBottom: 20,
              fontFamily: "'JetBrains Mono', monospace",
            }}
          >
            ✕ {dataError}
          </div>
        )}

        {/* stat row */}
        <div style={{ display: "flex", gap: 16, flexWrap: "wrap", marginBottom: 28 }}>
          <StatCard
            label="Total Events"
            value={totalEvents === null ? "—" : totalEvents.toLocaleString()}
            sub={lastSync ? `synced ${lastSync.toLocaleTimeString()}` : "syncing..."}
          />
          <StatCard
            label="Active Locations"
            value={Object.keys(locationCount).length || "—"}
          />
          <StatCard
            label="Pipeline Status"
            value={connStatus === "live" ? "ONLINE" : "ERROR"}
            sub="polling every 6s"
          />
        </div>

        <div style={{ display: "flex", gap: 20, flexWrap: "wrap" }}>
          {/* latest events */}
          <div
            style={{
              flex: "1 1 380px",
              background: "#11161A",
              border: "1px solid #1E2629",
              borderRadius: 4,
              padding: 22,
            }}
          >
            <div
              style={{
                fontSize: 11,
                letterSpacing: "0.08em",
                textTransform: "uppercase",
                color: "#6B7780",
                marginBottom: 16,
              }}
            >
              Latest Events
            </div>
            {latestEvents.length === 0 ? (
              <div style={{ color: "#4D5760", fontSize: 13, fontFamily: "'JetBrains Mono', monospace" }}>
                No events yet — waiting on pipeline.
              </div>
            ) : (
              <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
                {latestEvents.map((ev, i) => (
                  <div
                    key={ev.id || i}
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      borderBottom: i < latestEvents.length - 1 ? "1px solid #1A2024" : "none",
                      paddingBottom: 8,
                      fontFamily: "'JetBrains Mono', monospace",
                      fontSize: 13,
                    }}
                  >
                    <span style={{ color: "#EDF2F4" }}>{ev.location}</span>
                    <span style={{ color: "#4D5760" }}>
                      {ev.eventAt ? new Date(ev.eventAt).toLocaleTimeString() : ""}
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* location breakdown */}
          <div
            style={{
              flex: "1 1 380px",
              background: "#11161A",
              border: "1px solid #1E2629",
              borderRadius: 4,
              padding: 22,
            }}
          >
            <div
              style={{
                fontSize: 11,
                letterSpacing: "0.08em",
                textTransform: "uppercase",
                color: "#6B7780",
                marginBottom: 16,
              }}
            >
              Location Breakdown
            </div>
            {topLocations.length === 0 ? (
              <div style={{ color: "#4D5760", fontSize: 13, fontFamily: "'JetBrains Mono', monospace" }}>
                No location data yet.
              </div>
            ) : (
              <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
                {topLocations.map(([loc, count]) => (
                  <div key={loc}>
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                        fontSize: 12,
                        marginBottom: 5,
                        fontFamily: "'JetBrains Mono', monospace",
                      }}
                    >
                      <span>{loc}</span>
                      <span style={{ color: "#4D9B72" }}>{count}</span>
                    </div>
                    <div style={{ height: 5, background: "#1A2024", borderRadius: 3, overflow: "hidden" }}>
                      <div
                        style={{
                          height: "100%",
                          width: `${(count / maxLocCount) * 100}%`,
                          background: "#00FF94",
                          borderRadius: 3,
                          transition: "width 0.4s ease",
                        }}
                      />
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* AI summary */}
        <div
          style={{
            marginTop: 20,
            background: "#11161A",
            border: "1px solid #1E2629",
            borderRadius: 4,
            padding: 22,
          }}
        >
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              marginBottom: 16,
              flexWrap: "wrap",
              gap: 10,
            }}
          >
            <div style={{ fontSize: 11, letterSpacing: "0.08em", textTransform: "uppercase", color: "#6B7780" }}>
              AI Analyst Summary
            </div>
            <button
              onClick={handleGenerateSummary}
              disabled={summaryLoading}
              style={{
                background: summaryLoading ? "#1E2629" : "#00FF94",
                color: summaryLoading ? "#6B7780" : "#0A0E0F",
                border: "none",
                borderRadius: 4,
                padding: "8px 16px",
                fontFamily: "'JetBrains Mono', monospace",
                fontSize: 12,
                fontWeight: 600,
                letterSpacing: "0.04em",
                cursor: summaryLoading ? "default" : "pointer",
              }}
            >
              {summaryLoading ? "GENERATING..." : "GENERATE SUMMARY →"}
            </button>
          </div>

          {summaryError && (
            <div style={{ fontSize: 12, color: "#FF8080", fontFamily: "'JetBrains Mono', monospace" }}>
              ✕ {summaryError}
            </div>
          )}

          {!summary && !summaryError && !summaryLoading && (
            <div style={{ color: "#4D5760", fontSize: 13, fontFamily: "'JetBrains Mono', monospace" }}>
              No summary generated yet. Click the button to ask the analyst model.
            </div>
          )}

          {summaryLoading && (
            <div style={{ color: "#4D9B72", fontSize: 13, fontFamily: "'JetBrains Mono', monospace" }}>
              Reading dashboard metrics and composing summary...
            </div>
          )}

          {summary && (
            <div
              style={{
                fontSize: 14,
                lineHeight: 1.7,
                color: "#D6DCDF",
                whiteSpace: "pre-wrap",
                fontFamily: "Inter, sans-serif",
                animation: "fadeUp 0.3s ease",
              }}
            >
              {summary}
            </div>
          )}
        </div>

        <div
          style={{
            textAlign: "center",
            marginTop: 28,
            fontSize: 11,
            color: "#3A4248",
            fontFamily: "'JetBrains Mono', monospace",
          }}
        >
          auth :8084 · producer :8081 · consumer :8082 · query :8083 · ai :8085
        </div>
      </div>
    </div>
  );
}
