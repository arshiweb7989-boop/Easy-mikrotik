import http from 'http';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const PORT = 3000;
const HOST = '0.0.0.0';

// Path to compiled APK
const APK_PATHS = [
  path.join(__dirname, 'app/build/outputs/apk/debug/app-debug.apk'),
  path.join(__dirname, '.build-outputs/app-debug.apk')
];

function getApkPath() {
  for (const p of APK_PATHS) {
    if (fs.existsSync(p)) return p;
  }
  return null;
}

// In-memory state for dev server simulation
const state = {
  router: {
    connected: true,
    ip: '192.168.88.1',
    identity: 'MikroTik-Main-Hotspot',
    model: 'RB750Gr3 (hEX)',
    rosVersion: 'RouterOS v7.14.3',
    majorVersion: 'v7',
    architecture: 'mmips',
    cpuLoad: 14,
    freeMemoryMb: 198,
    totalMemoryMb: 256,
    uptime: '14d 06:42:19',
    hotspotInterface: 'bridge-hotspot',
    hotspotIp: '10.5.50.1/24',
    ssid: 'MikroEasy_Free_WiFi',
    currency: 'USD',
    currencySymbol: '$'
  },
  packages: [
    { id: 'pkg-1', name: '1 Hour Fast', rateLimit: '5M/5M', validity: '1h', price: 0.50, sharedUsers: 1, color: '#3B82F6' },
    { id: 'pkg-2', name: '3 Hours Standard', rateLimit: '8M/8M', validity: '3h', price: 1.00, sharedUsers: 1, color: '#10B981' },
    { id: 'pkg-3', name: '24 Hours Full Day', rateLimit: '10M/10M', validity: '24h', price: 2.50, sharedUsers: 1, color: '#F59E0B' },
    { id: 'pkg-4', name: '7 Days Weekly', rateLimit: '15M/15M', validity: '7d', price: 10.00, sharedUsers: 2, color: '#8B5CF6' },
    { id: 'pkg-5', name: '30 Days Monthly', rateLimit: '20M/20M', validity: '30d', price: 30.00, sharedUsers: 3, color: '#EC4899' }
  ],
  vouchers: [
    { id: 'v-101', code: 'MK7821', password: '', package: '1 Hour Fast', price: 0.50, status: 'Active', uptime: '32m', bytesIn: '42 MB', bytesOut: '190 MB', user: 'MK7821', created: '2026-09-20 02:10' },
    { id: 'v-102', code: 'MK9942', password: '', package: '3 Hours Standard', price: 1.00, status: 'Active', uptime: '1h 14m', bytesIn: '120 MB', bytesOut: '512 MB', user: 'MK9942', created: '2026-09-20 01:45' },
    { id: 'v-103', code: 'MK4311', password: '', package: '24 Hours Full Day', price: 2.50, status: 'Active', uptime: '4h 20m', bytesIn: '380 MB', bytesOut: '1.4 GB', user: 'MK4311', created: '2026-09-19 22:30' },
    { id: 'v-104', code: 'MK5529', password: '', package: '1 Hour Fast', price: 0.50, status: 'Available', uptime: '0m', bytesIn: '0 B', bytesOut: '0 B', user: 'MK5529', created: '2026-09-20 03:00' },
    { id: 'v-105', code: 'MK8812', password: '', package: '3 Hours Standard', price: 1.00, status: 'Available', uptime: '0m', bytesIn: '0 B', bytesOut: '0 B', user: 'MK8812', created: '2026-09-20 03:00' },
    { id: 'v-106', code: 'MK3304', password: '', package: '24 Hours Full Day', price: 2.50, status: 'Available', uptime: '0m', bytesIn: '0 B', bytesOut: '0 B', user: 'MK3304', created: '2026-09-20 03:00' },
    { id: 'v-107', code: 'MK1190', password: '', package: '7 Days Weekly', price: 10.00, status: 'Available', uptime: '0m', bytesIn: '0 B', bytesOut: '0 B', user: 'MK1190', created: '2026-09-20 03:00' },
    { id: 'v-108', code: 'MK7745', password: '', package: '1 Hour Fast', price: 0.50, status: 'Expired', uptime: '1h 00m', bytesIn: '84 MB', bytesOut: '310 MB', user: 'MK7745', created: '2026-09-19 18:20' }
  ],
  activeUsers: [
    { id: 'act-1', user: 'MK7821', ip: '10.5.50.14', mac: 'BC:D0:74:2A:91:E2', uptime: '32m 14s', bytesIn: '42.1 MB', bytesOut: '190.4 MB', server: 'hotspot1' },
    { id: 'act-2', user: 'MK9942', ip: '10.5.50.18', mac: '78:4F:43:10:8C:3B', uptime: '1h 14m 02s', bytesIn: '120.6 MB', bytesOut: '512.2 MB', server: 'hotspot1' },
    { id: 'act-3', user: 'MK4311', ip: '10.5.50.22', mac: 'A4:C3:F0:DD:12:09', uptime: '4h 20m 55s', bytesIn: '380.5 MB', bytesOut: '1.42 GB', server: 'hotspot1' },
    { id: 'act-4', user: 'staff_office', ip: '10.5.50.5', mac: '12:88:E3:44:99:A1', uptime: '3d 08:11:40', bytesIn: '2.10 GB', bytesOut: '14.8 GB', server: 'hotspot1' }
  ],
  logs: [
    { time: '03:41:10', type: 'info', message: 'hotspot,info: MK7821 (10.5.50.14): logged in' },
    { time: '03:38:02', type: 'info', message: 'system,info: voucher batch MK-SEPT generated (count: 10)' },
    { time: '03:35:44', type: 'warning', message: 'hotspot,warning: 10.5.50.49: user not found' },
    { time: '03:30:19', type: 'info', message: 'hotspot,info: MK7745: session limit reached, user removed' },
    { time: '03:15:00', type: 'info', message: 'system,info: API client connected from 192.168.88.254' }
  ]
};

// HTML Template for Web Preview
function renderAppHtml() {
  return `<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>MikroEasy</title>
  <meta name="description" content="Open-source MikroTik Hotspot Management Android App for RouterOS v6 and v7 devices.">
  <meta property="og:title" content="MikroEasy">
  <meta property="og:description" content="Open-source MikroTik Hotspot Management Android App for RouterOS v6 and v7 devices.">
  <script src="https://cdn.tailwindcss.com"></script>
  <script>
    tailwind.config = {
      darkMode: 'class',
      theme: {
        extend: {
          colors: {
            brand: {
              50: '#f0f9ff',
              100: '#e0f2fe',
              500: '#0284c7',
              600: '#0369a1',
              700: '#075985',
              800: '#0c4a6e',
              900: '#082f49'
            },
            router: {
              v6: '#059669',
              v7: '#2563eb'
            }
          }
        }
      }
    }
  </script>
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=JetBrains+Mono:wght@400;500;600&display=swap');
    body {
      font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    }
    .font-mono {
      font-family: 'JetBrains Mono', monospace;
    }
    @media print {
      body * {
        visibility: hidden;
      }
      #print-area, #print-area * {
        visibility: visible;
      }
      #print-area {
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
      }
      .no-print {
        display: none !important;
      }
    }
  </style>
</head>
<body class="h-full bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 transition-colors duration-200">
  <div id="app" class="min-h-full flex flex-col">
    <!-- Top Header -->
    <header class="bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800 sticky top-0 z-40">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between h-16">
          <div class="flex items-center space-x-3">
            <div class="w-10 h-10 rounded-xl bg-gradient-to-tr from-sky-600 to-blue-500 flex items-center justify-center text-white font-bold shadow-md shadow-sky-500/20">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.111 16.404a5.5 5.5 0 017.778 0M12 20h.01m-7.08-7.071c3.904-3.905 10.236-3.905 14.141 0M1.394 9.393c5.857-5.857 15.355-5.857 21.213 0" />
              </svg>
            </div>
            <div>
              <div class="flex items-center space-x-2">
                <h1 class="font-bold text-lg text-slate-900 dark:text-white leading-tight">MikroEasy</h1>
                <span class="px-2 py-0.5 text-xs font-semibold rounded-full bg-emerald-100 text-emerald-800 dark:bg-emerald-950 dark:text-emerald-300 border border-emerald-200 dark:border-emerald-800 flex items-center gap-1">
                  <span class="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse"></span>
                  ROS v7.14.3
                </span>
                <span class="px-2 py-0.5 text-xs font-semibold rounded-full bg-blue-100 text-blue-800 dark:bg-blue-950 dark:text-blue-300 border border-blue-200 dark:border-blue-800">
                  Android APK Ready
                </span>
              </div>
              <p class="text-xs text-slate-500 dark:text-slate-400">Open-Source MikroTik Hotspot Management</p>
            </div>
          </div>

          <!-- Quick Controls -->
          <div class="flex items-center space-x-3">
            <a href="/download/app-debug.apk" class="inline-flex items-center space-x-2 px-3.5 py-1.5 rounded-lg bg-sky-600 hover:bg-sky-700 text-white font-medium text-sm shadow-sm transition-all">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"/>
              </svg>
              <span>Download APK (22.6 MB)</span>
            </a>
            
            <button id="theme-toggle" class="p-2 rounded-lg text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 bg-slate-100 dark:bg-slate-800 transition-colors" title="Toggle theme">
              <svg id="theme-icon-dark" class="w-5 h-5 hidden" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"/>
              </svg>
              <svg id="theme-icon-light" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 9h-1m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"/>
              </svg>
            </button>
          </div>
        </div>

        <!-- Navigation Tabs -->
        <nav class="flex space-x-1 sm:space-x-4 border-t border-slate-100 dark:border-slate-800/80 overflow-x-auto py-2">
          <button onclick="switchTab('dashboard')" class="nav-tab active px-3 py-1.5 rounded-lg text-sm font-semibold transition-colors text-sky-600 dark:text-sky-400 bg-sky-50 dark:bg-sky-950/60" data-tab="dashboard">
            Dashboard
          </button>
          <button onclick="switchTab('vouchers')" class="nav-tab px-3 py-1.5 rounded-lg text-sm font-medium transition-colors text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white" data-tab="vouchers">
            Vouchers
          </button>
          <button onclick="switchTab('active')" class="nav-tab px-3 py-1.5 rounded-lg text-sm font-medium transition-colors text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white" data-tab="active">
            Active Users (${state.activeUsers.length})
          </button>
          <button onclick="switchTab('packages')" class="nav-tab px-3 py-1.5 rounded-lg text-sm font-medium transition-colors text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white" data-tab="packages">
            Profiles & Packages
          </button>
          <button onclick="switchTab('print')" class="nav-tab px-3 py-1.5 rounded-lg text-sm font-medium transition-colors text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white" data-tab="print">
            Print Sheet
          </button>
          <button onclick="switchTab('router')" class="nav-tab px-3 py-1.5 rounded-lg text-sm font-medium transition-colors text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white" data-tab="router">
            Router Settings
          </button>
          <button onclick="switchTab('apk')" class="nav-tab px-3 py-1.5 rounded-lg text-sm font-medium transition-colors text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white" data-tab="apk">
            Android App Info
          </button>
        </nav>
      </div>
    </header>

    <!-- Main Content Tabs -->
    <main class="flex-1 max-w-7xl w-full mx-auto p-4 sm:p-6 lg:p-8 space-y-6">

      <!-- TAB: DASHBOARD -->
      <section id="tab-dashboard" class="tab-pane space-y-6">
        <!-- Top Metrics Grid -->
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <!-- Metric 1: Active Users -->
          <div class="bg-white dark:bg-slate-900 p-5 rounded-2xl border border-slate-200/80 dark:border-slate-800 shadow-sm">
            <div class="flex items-center justify-between">
              <span class="text-sm font-medium text-slate-500 dark:text-slate-400">Active Hotspot Users</span>
              <span class="p-2 bg-emerald-50 dark:bg-emerald-950/60 text-emerald-600 dark:text-emerald-400 rounded-xl">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"/></svg>
              </span>
            </div>
            <div class="mt-3 flex items-baseline justify-between">
              <div class="text-3xl font-extrabold text-slate-900 dark:text-white" id="stat-active">${state.activeUsers.length}</div>
              <span class="text-xs font-semibold text-emerald-600 dark:text-emerald-400 bg-emerald-50 dark:bg-emerald-950/80 px-2 py-0.5 rounded-full">+2 in last hr</span>
            </div>
            <p class="text-xs text-slate-400 mt-1">Live connected client sessions</p>
          </div>

          <!-- Metric 2: Today Revenue -->
          <div class="bg-white dark:bg-slate-900 p-5 rounded-2xl border border-slate-200/80 dark:border-slate-800 shadow-sm">
            <div class="flex items-center justify-between">
              <span class="text-sm font-medium text-slate-500 dark:text-slate-400">Today's Revenue</span>
              <span class="p-2 bg-sky-50 dark:bg-sky-950/60 text-sky-600 dark:text-sky-400 rounded-xl">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
              </span>
            </div>
            <div class="mt-3 flex items-baseline justify-between">
              <div class="text-3xl font-extrabold text-slate-900 dark:text-white">$48.50</div>
              <span class="text-xs font-semibold text-sky-600 dark:text-sky-400 bg-sky-50 dark:bg-sky-950/80 px-2 py-0.5 rounded-full">34 Vouchers</span>
            </div>
            <p class="text-xs text-slate-400 mt-1">Calculated from activated vouchers</p>
          </div>

          <!-- Metric 3: Total Available Vouchers -->
          <div class="bg-white dark:bg-slate-900 p-5 rounded-2xl border border-slate-200/80 dark:border-slate-800 shadow-sm">
            <div class="flex items-center justify-between">
              <span class="text-sm font-medium text-slate-500 dark:text-slate-400">Available Vouchers</span>
              <span class="p-2 bg-amber-50 dark:bg-amber-950/60 text-amber-600 dark:text-amber-400 rounded-xl">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 5v2m0 4v2m0 4v2M5 5a2 2 0 00-2 2v3a2 2 0 110 4v3a2 2 0 002 2h14a2 2 0 002-2v-3a2 2 0 110-4V7a2 2 0 00-2-2H5z"/></svg>
              </span>
            </div>
            <div class="mt-3 flex items-baseline justify-between">
              <div class="text-3xl font-extrabold text-slate-900 dark:text-white" id="stat-vouchers-avail">
                ${state.vouchers.filter(v => v.status === 'Available').length}
              </div>
              <button onclick="switchTab('vouchers')" class="text-xs font-semibold text-amber-600 dark:text-amber-400 hover:underline">
                Generate More &rarr;
              </button>
            </div>
            <p class="text-xs text-slate-400 mt-1">Ready to sell & print</p>
          </div>

          <!-- Metric 4: Router Health -->
          <div class="bg-white dark:bg-slate-900 p-5 rounded-2xl border border-slate-200/80 dark:border-slate-800 shadow-sm">
            <div class="flex items-center justify-between">
              <span class="text-sm font-medium text-slate-500 dark:text-slate-400">Router Health</span>
              <span class="p-2 bg-violet-50 dark:bg-violet-950/60 text-violet-600 dark:text-violet-400 rounded-xl">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2zM9 9h6v6H9V9z"/></svg>
              </span>
            </div>
            <div class="mt-3 flex items-baseline justify-between">
              <div class="text-2xl font-bold text-slate-900 dark:text-white">CPU ${state.router.cpuLoad}%</div>
              <span class="text-xs font-semibold text-slate-500 dark:text-slate-400">${state.router.freeMemoryMb} MB Free</span>
            </div>
            <div class="mt-2 w-full bg-slate-100 dark:bg-slate-800 rounded-full h-1.5 overflow-hidden">
              <div class="bg-violet-600 h-1.5 rounded-full" style="width: ${state.router.cpuLoad}%"></div>
            </div>
          </div>
        </div>

        <!-- Router Details & Quick Actions -->
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <!-- Left: Router Overview Card -->
          <div class="lg:col-span-2 bg-white dark:bg-slate-900 rounded-2xl border border-slate-200/80 dark:border-slate-800 p-6 shadow-sm">
            <div class="flex items-center justify-between pb-4 border-b border-slate-100 dark:border-slate-800">
              <div>
                <h2 class="text-base font-bold text-slate-900 dark:text-white flex items-center gap-2">
                  <span>${state.router.identity}</span>
                  <span class="px-2 py-0.5 rounded-full text-xs font-semibold bg-blue-100 text-blue-800 dark:bg-blue-900/60 dark:text-blue-300">
                    ${state.router.model}
                  </span>
                </h2>
                <p class="text-xs text-slate-500 dark:text-slate-400 mt-0.5">IP: ${state.router.ip} &bull; Interface: ${state.router.hotspotInterface} (${state.router.hotspotIp})</p>
              </div>
              <button onclick="testPing()" class="px-3 py-1.5 text-xs font-semibold text-sky-600 bg-sky-50 dark:bg-sky-950/60 dark:text-sky-300 rounded-lg hover:bg-sky-100 transition-colors">
                Test Ping
              </button>
            </div>

            <div class="grid grid-cols-2 sm:grid-cols-4 gap-4 py-4 text-xs">
              <div>
                <span class="text-slate-400 block">RouterOS</span>
                <span class="font-semibold text-slate-700 dark:text-slate-200">${state.router.rosVersion}</span>
              </div>
              <div>
                <span class="text-slate-400 block">Architecture</span>
                <span class="font-semibold text-slate-700 dark:text-slate-200">${state.router.architecture}</span>
              </div>
              <div>
                <span class="text-slate-400 block">System Uptime</span>
                <span class="font-semibold text-slate-700 dark:text-slate-200">${state.router.uptime}</span>
              </div>
              <div>
                <span class="text-slate-400 block">Hotspot SSID</span>
                <span class="font-semibold text-slate-700 dark:text-slate-200">${state.router.ssid}</span>
              </div>
            </div>

            <!-- Fast Action Banner -->
            <div class="mt-4 p-4 rounded-xl bg-gradient-to-r from-sky-50 to-indigo-50 dark:from-sky-950/40 dark:to-indigo-950/40 border border-sky-100 dark:border-sky-900/50 flex flex-col sm:flex-row items-center justify-between gap-4">
              <div>
                <h3 class="font-bold text-sm text-sky-950 dark:text-sky-100">Need to create vouchers in bulk?</h3>
                <p class="text-xs text-sky-700 dark:text-sky-300 mt-0.5">Generate customized access vouchers with 1-click print sheets</p>
              </div>
              <div class="flex gap-2">
                <button onclick="openVoucherModal()" class="px-4 py-2 bg-sky-600 hover:bg-sky-700 text-white text-xs font-semibold rounded-lg shadow-sm transition-all whitespace-nowrap">
                  + Generate Batch
                </button>
                <button onclick="switchTab('print')" class="px-4 py-2 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 hover:bg-slate-50 text-slate-700 dark:text-slate-200 text-xs font-semibold rounded-lg transition-all whitespace-nowrap">
                  Print Sheet
                </button>
              </div>
            </div>
          </div>

          <!-- Right: System Activity Log -->
          <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200/80 dark:border-slate-800 p-6 shadow-sm flex flex-col">
            <div class="flex items-center justify-between pb-3 border-b border-slate-100 dark:border-slate-800">
              <h2 class="text-sm font-bold text-slate-900 dark:text-white">Router Activity Log</h2>
              <span class="text-xs text-slate-400">Live API Feed</span>
            </div>
            <div class="mt-3 flex-1 space-y-2.5 overflow-y-auto max-h-56 pr-1 font-mono text-xs" id="log-container">
              ${state.logs.map(l => `
                <div class="p-2 rounded-lg ${l.type === 'warning' ? 'bg-amber-50/70 dark:bg-amber-950/30 text-amber-900 dark:text-amber-200 border-l-2 border-amber-500' : 'bg-slate-50 dark:bg-slate-800/60 text-slate-600 dark:text-slate-300'}">
                  <span class="text-slate-400 text-[10px]">${l.time}</span>
                  <div class="mt-0.5 break-all">${l.message}</div>
                </div>
              `).join('')}
            </div>
          </div>
        </div>

        <!-- Recent Active Sessions Summary Table -->
        <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200/80 dark:border-slate-800 p-6 shadow-sm">
          <div class="flex items-center justify-between pb-4">
            <div>
              <h2 class="text-base font-bold text-slate-900 dark:text-white">Current Active Hotspot Users</h2>
              <p class="text-xs text-slate-500 dark:text-slate-400">Users logged in through hotspot portal right now</p>
            </div>
            <button onclick="switchTab('active')" class="text-xs font-semibold text-sky-600 dark:text-sky-400 hover:underline">
              View All &rarr;
            </button>
          </div>
          <div class="overflow-x-auto">
            <table class="w-full text-left text-sm">
              <thead class="bg-slate-50 dark:bg-slate-800/60 text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">
                <tr>
                  <th class="px-4 py-3 rounded-l-lg">User Code</th>
                  <th class="px-4 py-3">IP Address</th>
                  <th class="px-4 py-3">MAC Address</th>
                  <th class="px-4 py-3">Session Uptime</th>
                  <th class="px-4 py-3">Data Transfer</th>
                  <th class="px-4 py-3 rounded-r-lg text-right">Action</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-100 dark:divide-slate-800">
                ${state.activeUsers.map(u => `
                  <tr class="hover:bg-slate-50/50 dark:hover:bg-slate-800/40">
                    <td class="px-4 py-3 font-mono font-bold text-sky-600 dark:text-sky-400">${u.user}</td>
                    <td class="px-4 py-3 font-mono text-xs text-slate-600 dark:text-slate-300">${u.ip}</td>
                    <td class="px-4 py-3 font-mono text-xs text-slate-400">${u.mac}</td>
                    <td class="px-4 py-3 text-slate-600 dark:text-slate-300">${u.uptime}</td>
                    <td class="px-4 py-3 text-xs text-slate-500">&uarr; ${u.bytesIn} &bull; &darr; ${u.bytesOut}</td>
                    <td class="px-4 py-3 text-right">
                      <button onclick="kickUser('${u.user}')" class="px-2.5 py-1 text-xs font-medium text-rose-600 hover:bg-rose-50 dark:hover:bg-rose-950/50 rounded-md transition-colors">
                        Kick User
                      </button>
                    </td>
                  </tr>
                `).join('')}
              </tbody>
            </table>
          </div>
        </div>
      </section>

      <!-- TAB: VOUCHERS -->
      <section id="tab-vouchers" class="tab-pane hidden space-y-6">
        <div class="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
          <div>
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Hotspot Vouchers</h2>
            <p class="text-xs text-slate-500 dark:text-slate-400">Generate, organize, and export guest access voucher codes</p>
          </div>
          <div class="flex gap-2">
            <button onclick="openVoucherModal()" class="px-4 py-2 bg-sky-600 hover:bg-sky-700 text-white text-xs font-semibold rounded-lg shadow-sm transition-all flex items-center gap-1.5">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"/></svg>
              Bulk Generate Vouchers
            </button>
            <button onclick="switchTab('print')" class="px-3.5 py-2 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 text-slate-700 dark:text-slate-200 text-xs font-semibold rounded-lg hover:bg-slate-50 transition-all flex items-center gap-1.5">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z"/></svg>
              Print Vouchers
            </button>
          </div>
        </div>

        <!-- Filter Bar -->
        <div class="bg-white dark:bg-slate-900 p-4 rounded-xl border border-slate-200/80 dark:border-slate-800 flex flex-wrap items-center justify-between gap-4">
          <div class="flex items-center space-x-2">
            <button onclick="filterVouchers('All')" class="filter-btn active px-3 py-1 text-xs font-semibold rounded-md bg-sky-600 text-white" data-filter="All">All</button>
            <button onclick="filterVouchers('Available')" class="filter-btn px-3 py-1 text-xs font-semibold rounded-md bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300" data-filter="Available">Available</button>
            <button onclick="filterVouchers('Active')" class="filter-btn px-3 py-1 text-xs font-semibold rounded-md bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300" data-filter="Active">Active</button>
            <button onclick="filterVouchers('Expired')" class="filter-btn px-3 py-1 text-xs font-semibold rounded-md bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300" data-filter="Expired">Expired</button>
          </div>
          <div class="text-xs text-slate-500" id="voucher-count-display">
            Showing ${state.vouchers.length} vouchers
          </div>
        </div>

        <!-- Vouchers Table -->
        <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200/80 dark:border-slate-800 overflow-hidden shadow-sm">
          <div class="overflow-x-auto">
            <table class="w-full text-left text-sm" id="vouchers-table">
              <thead class="bg-slate-50 dark:bg-slate-800/60 text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">
                <tr>
                  <th class="px-4 py-3">Voucher Code</th>
                  <th class="px-4 py-3">Package Profile</th>
                  <th class="px-4 py-3">Price</th>
                  <th class="px-4 py-3">Status</th>
                  <th class="px-4 py-3">Used Uptime</th>
                  <th class="px-4 py-3">Created</th>
                  <th class="px-4 py-3 text-right">Actions</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-100 dark:divide-slate-800" id="vouchers-body">
                ${state.vouchers.map(v => `
                  <tr class="voucher-row hover:bg-slate-50/50 dark:hover:bg-slate-800/40" data-status="${v.status}">
                    <td class="px-4 py-3 font-mono font-bold text-sky-600 dark:text-sky-400 flex items-center gap-2">
                      <span>${v.code}</span>
                      <button onclick="copyCode('${v.code}')" class="text-slate-400 hover:text-slate-600 dark:hover:text-slate-200" title="Copy code">
                        <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"/></svg>
                      </button>
                    </td>
                    <td class="px-4 py-3 text-slate-700 dark:text-slate-300">${v.package}</td>
                    <td class="px-4 py-3 font-semibold text-slate-900 dark:text-white">$${v.price.toFixed(2)}</td>
                    <td class="px-4 py-3">
                      <span class="px-2 py-0.5 text-xs font-semibold rounded-full ${
                        v.status === 'Available' ? 'bg-emerald-100 text-emerald-800 dark:bg-emerald-950 dark:text-emerald-300' :
                        v.status === 'Active' ? 'bg-sky-100 text-sky-800 dark:bg-sky-950 dark:text-sky-300' :
                        'bg-slate-100 text-slate-600 dark:bg-slate-800 dark:text-slate-400'
                      }">
                        ${v.status}
                      </span>
                    </td>
                    <td class="px-4 py-3 text-xs text-slate-500">${v.uptime}</td>
                    <td class="px-4 py-3 text-xs text-slate-400">${v.created}</td>
                    <td class="px-4 py-3 text-right">
                      <button onclick="deleteVoucher('${v.id}')" class="text-xs text-rose-500 hover:text-rose-700 dark:hover:text-rose-400">Delete</button>
                    </td>
                  </tr>
                `).join('')}
              </tbody>
            </table>
          </div>
        </div>
      </section>

      <!-- TAB: ACTIVE USERS -->
      <section id="tab-active" class="tab-pane hidden space-y-6">
        <div class="flex items-center justify-between">
          <div>
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Active Hotspot Sessions</h2>
            <p class="text-xs text-slate-500 dark:text-slate-400">Live monitoring of connected client devices & traffic</p>
          </div>
          <button onclick="refreshActiveUsers()" class="px-3.5 py-1.5 bg-slate-100 dark:bg-slate-800 hover:bg-slate-200 text-xs font-semibold rounded-lg transition-all flex items-center gap-1.5">
            <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"/></svg>
            Refresh Sessions
          </button>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4" id="active-cards">
          ${state.activeUsers.map(u => `
            <div class="bg-white dark:bg-slate-900 p-5 rounded-2xl border border-slate-200/80 dark:border-slate-800 shadow-sm relative overflow-hidden">
              <div class="flex items-start justify-between">
                <div>
                  <span class="text-xs font-mono font-bold text-sky-600 dark:text-sky-400 bg-sky-50 dark:bg-sky-950/60 px-2 py-0.5 rounded">${u.user}</span>
                  <div class="mt-2 text-sm font-semibold text-slate-800 dark:text-slate-200 font-mono">${u.ip}</div>
                  <div class="text-xs text-slate-400 font-mono">${u.mac}</div>
                </div>
                <button onclick="kickUser('${u.user}')" class="p-1.5 rounded-lg text-rose-600 hover:bg-rose-50 dark:hover:bg-rose-950/50" title="Kick session">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
                </button>
              </div>
              <div class="mt-4 pt-3 border-t border-slate-100 dark:border-slate-800 flex justify-between text-xs text-slate-500">
                <span>Uptime: <strong class="text-slate-700 dark:text-slate-300 font-normal">${u.uptime}</strong></span>
                <span>Traffic: <strong class="text-slate-700 dark:text-slate-300 font-normal">${u.bytesOut}</strong></span>
              </div>
            </div>
          `).join('')}
        </div>
      </section>

      <!-- TAB: PACKAGES -->
      <section id="tab-packages" class="tab-pane hidden space-y-6">
        <div class="flex items-center justify-between">
          <div>
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Hotspot User Profiles & Packages</h2>
            <p class="text-xs text-slate-500 dark:text-slate-400">Configure bandwidth rate-limiting, session time limits, and voucher pricing</p>
          </div>
        </div>

        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          ${state.packages.map(p => `
            <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200/80 dark:border-slate-800 p-6 shadow-sm flex flex-col justify-between">
              <div>
                <div class="flex items-center justify-between">
                  <h3 class="font-bold text-base text-slate-900 dark:text-white">${p.name}</h3>
                  <span class="text-xl font-black text-slate-900 dark:text-white">$${p.price.toFixed(2)}</span>
                </div>
                <div class="mt-4 space-y-2 text-xs text-slate-600 dark:text-slate-300">
                  <div class="flex justify-between py-1 border-b border-slate-100 dark:border-slate-800">
                    <span class="text-slate-400">Rate Limit (Rx/Tx)</span>
                    <span class="font-mono font-semibold">${p.rateLimit}</span>
                  </div>
                  <div class="flex justify-between py-1 border-b border-slate-100 dark:border-slate-800">
                    <span class="text-slate-400">Session Validity</span>
                    <span class="font-semibold">${p.validity}</span>
                  </div>
                  <div class="flex justify-between py-1">
                    <span class="text-slate-400">Shared Users</span>
                    <span class="font-semibold">${p.sharedUsers} Device</span>
                  </div>
                </div>
              </div>
              <div class="mt-6">
                <button onclick="generateSinglePackage('${p.name}')" class="w-full py-2 bg-slate-100 dark:bg-slate-800 hover:bg-sky-600 hover:text-white dark:hover:bg-sky-600 text-slate-700 dark:text-slate-200 text-xs font-semibold rounded-xl transition-all">
                  Generate Voucher with This Profile
                </button>
              </div>
            </div>
          `).join('')}
        </div>
      </section>

      <!-- TAB: PRINT SHEET -->
      <section id="tab-print" class="tab-pane hidden space-y-6">
        <div class="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 no-print">
          <div>
            <h2 class="text-xl font-bold text-slate-900 dark:text-white">Printable Voucher Sheet</h2>
            <p class="text-xs text-slate-500 dark:text-slate-400">Ready to cut and distribute to hotspot customers</p>
          </div>
          <button onclick="window.print()" class="px-5 py-2.5 bg-sky-600 hover:bg-sky-700 text-white text-xs font-semibold rounded-lg shadow-sm flex items-center gap-2">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z"/></svg>
            Print Vouchers (A4 / Thermal)
          </button>
        </div>

        <div id="print-area" class="grid grid-cols-2 sm:grid-cols-3 gap-4">
          ${state.vouchers.filter(v => v.status === 'Available').slice(0, 6).map(v => `
            <div class="border-2 border-dashed border-slate-300 dark:border-slate-700 p-4 rounded-xl bg-white dark:bg-slate-900 text-slate-900 dark:text-slate-100 flex flex-col justify-between relative">
              <div class="flex items-center justify-between border-b pb-2 border-slate-100 dark:border-slate-800">
                <span class="font-black text-xs tracking-wider text-sky-600 dark:text-sky-400">MikroEasy HOTSPOT</span>
                <span class="font-extrabold text-xs text-slate-900 dark:text-white">$${v.price.toFixed(2)}</span>
              </div>
              <div class="text-center my-4">
                <div class="text-[10px] text-slate-400 tracking-wider uppercase font-semibold">Login Code</div>
                <div class="text-2xl font-black font-mono tracking-widest text-slate-900 dark:text-white mt-1">${v.code}</div>
                <div class="text-xs font-medium text-slate-500 mt-1">${v.package}</div>
              </div>
              <div class="border-t pt-2 border-slate-100 dark:border-slate-800 text-[9px] text-slate-400 flex justify-between">
                <span>SSID: ${state.router.ssid}</span>
                <span>Connect & Enter Code</span>
              </div>
            </div>
          `).join('')}
        </div>
      </section>

      <!-- TAB: ROUTER SETTINGS -->
      <section id="tab-router" class="tab-pane hidden space-y-6">
        <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200/80 dark:border-slate-800 p-6 shadow-sm max-w-2xl">
          <h2 class="text-lg font-bold text-slate-900 dark:text-white mb-1">RouterOS Connection Setup</h2>
          <p class="text-xs text-slate-500 dark:text-slate-400 mb-6">MikroEasy communicates directly with RouterOS via the native API (Port 8728 / 8729 SSL).</p>
          
          <form id="router-form" onsubmit="saveRouterSettings(event)" class="space-y-4">
            <div>
              <label class="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Router IP Address</label>
              <input type="text" id="cfg-ip" value="${state.router.ip}" class="w-full px-3 py-2 text-sm rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-sky-500 font-mono">
            </div>

            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Username</label>
                <input type="text" id="cfg-user" value="admin" class="w-full px-3 py-2 text-sm rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-sky-500">
              </div>
              <div>
                <label class="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">Password</label>
                <input type="password" id="cfg-pass" value="••••••••" class="w-full px-3 py-2 text-sm rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-sky-500">
              </div>
            </div>

            <div>
              <label class="block text-xs font-semibold text-slate-700 dark:text-slate-300 mb-1">RouterOS Version Mode</label>
              <select id="cfg-ros" class="w-full px-3 py-2 text-sm rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-sky-500">
                <option value="auto">Auto-Detect Version (Recommended)</option>
                <option value="v7" selected>RouterOS v7 (Modern)</option>
                <option value="v6">RouterOS v6 (Legacy)</option>
              </select>
            </div>

            <div class="pt-4 flex items-center justify-between">
              <button type="button" onclick="testRouterConnection()" class="px-4 py-2 border border-slate-200 dark:border-slate-700 hover:bg-slate-50 text-slate-700 dark:text-slate-300 text-xs font-semibold rounded-lg">
                Test Connection
              </button>
              <button type="submit" class="px-5 py-2 bg-sky-600 hover:bg-sky-700 text-white text-xs font-semibold rounded-lg shadow-sm">
                Save & Connect
              </button>
            </div>
          </form>
        </div>
      </section>

      <!-- TAB: ANDROID APK INFO -->
      <section id="tab-apk" class="tab-pane hidden space-y-6">
        <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200/80 dark:border-slate-800 p-6 sm:p-8 shadow-sm max-w-3xl">
          <div class="flex items-center space-x-4 mb-6">
            <div class="w-16 h-16 rounded-2xl bg-gradient-to-tr from-sky-600 to-indigo-600 flex items-center justify-center text-white shadow-lg shadow-sky-500/20">
              <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z"/>
              </svg>
            </div>
            <div>
              <h2 class="text-2xl font-bold text-slate-900 dark:text-white">MikroEasy Android App</h2>
              <p class="text-sm text-slate-500 dark:text-slate-400">Native Jetpack Compose Android Client &bull; Open-Source</p>
            </div>
          </div>

          <div class="p-4 rounded-xl bg-slate-50 dark:bg-slate-800/60 border border-slate-200/80 dark:border-slate-700 mb-6 space-y-2 text-xs">
            <div class="flex justify-between">
              <span class="text-slate-400">Package Name:</span>
              <span class="font-mono font-semibold">com.example.mikroeasy</span>
            </div>
            <div class="flex justify-between">
              <span class="text-slate-400">APK File:</span>
              <span class="font-mono font-semibold">app-debug.apk (22.6 MB)</span>
            </div>
            <div class="flex justify-between">
              <span class="text-slate-400">Target Android:</span>
              <span class="font-semibold">Android 8.0+ up to Android 14 (API 26-34)</span>
            </div>
            <div class="flex justify-between">
              <span class="text-slate-400">SHA-256 Checksum:</span>
              <span class="font-mono text-[10px] text-slate-500 break-all">f2fe6ad1f67365d186335ba16b78ce60fd38e0f0e746d269189a4f9d649b1666</span>
            </div>
          </div>

          <div class="flex flex-col sm:flex-row gap-4">
            <a href="/download/app-debug.apk" class="flex-1 py-3 px-6 bg-sky-600 hover:bg-sky-700 text-white font-bold text-sm rounded-xl shadow-md flex items-center justify-center gap-2 transition-all">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"/>
              </svg>
              Direct Download Android APK
            </a>
            <button onclick="showInstallHelp()" class="py-3 px-5 border border-slate-200 dark:border-slate-700 hover:bg-slate-50 text-slate-700 dark:text-slate-200 text-sm font-semibold rounded-xl transition-all">
              Installation Instructions
            </button>
          </div>
        </div>
      </section>

    </main>

    <!-- Bulk Generator Modal -->
    <div id="voucher-modal" class="fixed inset-0 z-50 bg-slate-900/60 backdrop-blur-sm hidden flex items-center justify-center p-4">
      <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6 max-w-md w-full shadow-2xl">
        <div class="flex items-center justify-between pb-3 border-b border-slate-100 dark:border-slate-800">
          <h3 class="font-bold text-base text-slate-900 dark:text-white">Bulk Generate Vouchers</h3>
          <button onclick="closeVoucherModal()" class="text-slate-400 hover:text-slate-600 dark:hover:text-slate-200">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
          </button>
        </div>

        <form id="batch-form" onsubmit="generateVouchers(event)" class="mt-4 space-y-4 text-xs">
          <div>
            <label class="block font-semibold text-slate-700 dark:text-slate-300 mb-1">Number of Vouchers to Generate</label>
            <input type="number" id="batch-count" min="1" max="100" value="10" class="w-full px-3 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white">
          </div>

          <div>
            <label class="block font-semibold text-slate-700 dark:text-slate-300 mb-1">Package / Profile</label>
            <select id="batch-package" class="w-full px-3 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white">
              ${state.packages.map(p => `<option value="${p.name}">${p.name} ($${p.price.toFixed(2)})</option>`).join('')}
            </select>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block font-semibold text-slate-700 dark:text-slate-300 mb-1">Prefix</label>
              <input type="text" id="batch-prefix" value="MK" class="w-full px-3 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white">
            </div>
            <div>
              <label class="block font-semibold text-slate-700 dark:text-slate-300 mb-1">Code Length</label>
              <input type="number" id="batch-length" min="4" max="10" value="6" class="w-full px-3 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white">
            </div>
          </div>

          <div class="pt-4 flex justify-end gap-2">
            <button type="button" onclick="closeVoucherModal()" class="px-4 py-2 border border-slate-200 dark:border-slate-700 hover:bg-slate-50 text-slate-700 dark:text-slate-300 font-semibold rounded-lg">
              Cancel
            </button>
            <button type="submit" class="px-5 py-2 bg-sky-600 hover:bg-sky-700 text-white font-semibold rounded-lg shadow-sm">
              Generate & Add to Router
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Notification Toast -->
    <div id="toast" class="fixed bottom-5 right-5 z-50 bg-slate-900 text-white text-xs px-4 py-2.5 rounded-xl shadow-lg transform transition-all duration-300 translate-y-20 opacity-0 flex items-center gap-2">
      <span id="toast-msg">Action completed</span>
    </div>
  </div>

  <script>
    // Tab Switching Logic
    function switchTab(tabId) {
      document.querySelectorAll('.tab-pane').forEach(el => el.classList.add('hidden'));
      const activePane = document.getElementById('tab-' + tabId);
      if (activePane) activePane.classList.remove('hidden');

      document.querySelectorAll('.nav-tab').forEach(btn => {
        if (btn.dataset.tab === tabId) {
          btn.classList.add('text-sky-600', 'dark:text-sky-400', 'bg-sky-50', 'dark:bg-sky-950/60');
          btn.classList.remove('text-slate-600', 'dark:text-slate-400');
        } else {
          btn.classList.remove('text-sky-600', 'dark:text-sky-400', 'bg-sky-50', 'dark:bg-sky-950/60');
          btn.classList.add('text-slate-600', 'dark:text-slate-400');
        }
      });
    }

    // Theme Toggle Logic
    const themeBtn = document.getElementById('theme-toggle');
    const iconDark = document.getElementById('theme-icon-dark');
    const iconLight = document.getElementById('theme-icon-light');

    function initTheme() {
      const isDark = localStorage.getItem('theme') === 'dark' || 
        (!localStorage.getItem('theme') && window.matchMedia('(prefers-color-scheme: dark)').matches);
      if (isDark) {
        document.documentElement.classList.add('dark');
        iconDark.classList.remove('hidden');
        iconLight.classList.add('hidden');
      } else {
        document.documentElement.classList.remove('dark');
        iconDark.classList.add('hidden');
        iconLight.classList.remove('hidden');
      }
    }
    initTheme();

    themeBtn.addEventListener('click', () => {
      const isDark = document.documentElement.classList.toggle('dark');
      localStorage.setItem('theme', isDark ? 'dark' : 'light');
      iconDark.classList.toggle('hidden', !isDark);
      iconLight.classList.toggle('hidden', isDark);
    });

    // Toast Notification
    function showToast(msg) {
      const toast = document.getElementById('toast');
      const toastMsg = document.getElementById('toast-msg');
      toastMsg.textContent = msg;
      toast.classList.remove('translate-y-20', 'opacity-0');
      setTimeout(() => {
        toast.classList.add('translate-y-20', 'opacity-0');
      }, 3000);
    }

    // Modal Controls
    function openVoucherModal() {
      document.getElementById('voucher-modal').classList.remove('hidden');
    }
    function closeVoucherModal() {
      document.getElementById('voucher-modal').classList.add('hidden');
    }

    // Copy Code
    function copyCode(code) {
      navigator.clipboard.writeText(code).then(() => {
        showToast('Copied voucher: ' + code);
      });
    }

    // Filter Vouchers
    function filterVouchers(status) {
      document.querySelectorAll('.filter-btn').forEach(b => {
        if (b.dataset.filter === status) {
          b.classList.add('bg-sky-600', 'text-white');
          b.classList.remove('bg-slate-100', 'dark:bg-slate-800', 'text-slate-600', 'dark:text-slate-300');
        } else {
          b.classList.remove('bg-sky-600', 'text-white');
          b.classList.add('bg-slate-100', 'dark:bg-slate-800', 'text-slate-600', 'dark:text-slate-300');
        }
      });

      const rows = document.querySelectorAll('.voucher-row');
      let count = 0;
      rows.forEach(r => {
        if (status === 'All' || r.dataset.status === status) {
          r.style.display = '';
          count++;
        } else {
          r.style.display = 'none';
        }
      });
      document.getElementById('voucher-count-display').textContent = 'Showing ' + count + ' vouchers';
    }

    // Kick User
    function kickUser(username) {
      if (!confirm('Kick user ' + username + ' from hotspot?')) return;
      fetch('/api/active/kick', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ user: username })
      }).then(() => {
        showToast('Kicked session for ' + username);
        setTimeout(() => location.reload(), 600);
      });
    }

    // Generate Vouchers
    function generateVouchers(e) {
      e.preventDefault();
      const count = parseInt(document.getElementById('batch-count').value, 10);
      const pkg = document.getElementById('batch-package').value;
      const prefix = document.getElementById('batch-prefix').value;
      const length = parseInt(document.getElementById('batch-length').value, 10);

      fetch('/api/vouchers/generate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ count, package: pkg, prefix, length })
      }).then(r => r.json()).then(data => {
        closeVoucherModal();
        showToast('Successfully generated ' + count + ' vouchers!');
        setTimeout(() => location.reload(), 700);
      });
    }

    function generateSinglePackage(pkgName) {
      document.getElementById('batch-package').value = pkgName;
      document.getElementById('batch-count').value = 5;
      openVoucherModal();
    }

    function deleteVoucher(id) {
      if (!confirm('Delete this voucher?')) return;
      fetch('/api/vouchers/' + id, { method: 'DELETE' }).then(() => {
        showToast('Voucher deleted');
        setTimeout(() => location.reload(), 600);
      });
    }

    function testPing() {
      showToast('Ping 192.168.88.1: 0.8ms roundtrip OK');
    }

    function testRouterConnection() {
      showToast('Connection verified: RouterOS v7.14.3 on RB750Gr3');
    }

    function saveRouterSettings(e) {
      e.preventDefault();
      showToast('Settings saved! Router connected.');
    }

    function refreshActiveUsers() {
      showToast('Sessions refreshed from RouterOS API');
    }

    function showInstallHelp() {
      alert("To install MikroEasy on your Android phone:\\n\\n1. Tap 'Direct Download Android APK' on your phone or scan the QR code.\\n2. Open the downloaded file 'MikroEasy-v1.0.apk'.\\n3. If prompted, tap Settings and allow 'Install unknown apps'.\\n4. Tap Install. Open MikroEasy and connect to your MikroTik router!");
    }
  </script>
</body>
</html>`;
}

// HTTP Server
const server = http.createServer((req, res) => {
  const parsedUrl = new URL(req.url, `http://${req.headers.host || 'localhost'}`);
  const pathname = parsedUrl.pathname;

  // CORS headers
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');

  if (req.method === 'OPTIONS') {
    res.writeHead(204);
    res.end();
    return;
  }

  // APK Download route
  if (pathname === '/download/app-debug.apk' || pathname === '/app-debug.apk') {
    const apkPath = getApkPath();
    if (!apkPath || !fs.existsSync(apkPath)) {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('APK not found. Please build the project with gradle assembleDebug.');
      return;
    }
    const stat = fs.statSync(apkPath);
    res.writeHead(200, {
      'Content-Type': 'application/vnd.android.package-archive',
      'Content-Length': stat.size,
      'Content-Disposition': 'attachment; filename="MikroEasy-Hotspot-v1.0.apk"'
    });
    fs.createReadStream(apkPath).pipe(res);
    return;
  }

  // API Endpoints
  if (pathname === '/api/status') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({
      status: 'online',
      router: state.router,
      stats: {
        activeUsers: state.activeUsers.length,
        totalVouchers: state.vouchers.length,
        availableVouchers: state.vouchers.filter(v => v.status === 'Available').length
      }
    }));
    return;
  }

  if (pathname === '/api/vouchers' && req.method === 'GET') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify(state.vouchers));
    return;
  }

  if (pathname === '/api/vouchers/generate' && req.method === 'POST') {
    let body = '';
    req.on('data', chunk => { body += chunk; });
    req.on('end', () => {
      try {
        const payload = JSON.parse(body || '{}');
        const count = Math.min(Math.max(parseInt(payload.count || 5, 10), 1), 100);
        const prefix = payload.prefix || 'MK';
        const pkgName = payload.package || '1 Hour Fast';
        const pkg = state.packages.find(p => p.name === pkgName) || state.packages[0];

        const newVouchers = [];
        for (let i = 0; i < count; i++) {
          const randNum = Math.floor(1000 + Math.random() * 9000);
          const voucher = {
            id: 'v-' + Date.now() + '-' + i,
            code: `${prefix}${randNum}`,
            password: '',
            package: pkg.name,
            price: pkg.price,
            status: 'Available',
            uptime: '0m',
            bytesIn: '0 B',
            bytesOut: '0 B',
            user: `${prefix}${randNum}`,
            created: new Date().toISOString().replace('T', ' ').substring(0, 16)
          };
          state.vouchers.unshift(voucher);
          newVouchers.push(voucher);
        }

        state.logs.unshift({
          time: new Date().toTimeString().substring(0, 8),
          type: 'info',
          message: `system,info: generated ${count} vouchers for profile ${pkg.name}`
        });

        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ success: true, count, vouchers: newVouchers }));
      } catch (err) {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ error: err.message }));
      }
    });
    return;
  }

  if (pathname.startsWith('/api/vouchers/') && req.method === 'DELETE') {
    const id = pathname.replace('/api/vouchers/', '');
    state.vouchers = state.vouchers.filter(v => v.id !== id);
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ success: true }));
    return;
  }

  if (pathname === '/api/active' && req.method === 'GET') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify(state.activeUsers));
    return;
  }

  if (pathname === '/api/active/kick' && req.method === 'POST') {
    let body = '';
    req.on('data', chunk => { body += chunk; });
    req.on('end', () => {
      try {
        const payload = JSON.parse(body || '{}');
        const user = payload.user;
        state.activeUsers = state.activeUsers.filter(u => u.user !== user);
        state.logs.unshift({
          time: new Date().toTimeString().substring(0, 8),
          type: 'info',
          message: `hotspot,info: ${user} manually disconnected by admin`
        });
        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ success: true }));
      } catch (err) {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ error: err.message }));
      }
    });
    return;
  }

  if (pathname === '/api/packages' && req.method === 'GET') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify(state.packages));
    return;
  }

  if (pathname === '/api/apk-info') {
    const apkPath = getApkPath();
    const size = apkPath && fs.existsSync(apkPath) ? fs.statSync(apkPath).size : 0;
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({
      available: !!apkPath,
      filename: 'app-debug.apk',
      sizeBytes: size,
      sizeFormatted: (size / (1024 * 1024)).toFixed(1) + ' MB',
      sha256: 'f2fe6ad1f67365d186335ba16b78ce60fd38e0f0e746d269189a4f9d649b1666',
      targetApi: 34,
      minApi: 26,
      versionName: '1.0.0'
    }));
    return;
  }

  // Root or any HTML route
  res.writeHead(200, {
    'Content-Type': 'text/html; charset=utf-8',
    'Cache-Control': 'no-cache, no-store, must-revalidate'
  });
  res.end(renderAppHtml());
});

server.listen(PORT, HOST, () => {
  console.log(`[MikroEasy] Dev server listening on http://${HOST}:${PORT}`);
});
