import { Component, type ReactNode, type ErrorInfo } from 'react';

interface State {
  error: Error | null;
}

export class ErrorBoundary extends Component<{ children: ReactNode }, State> {
  constructor(props: { children: ReactNode }) {
    super(props);
    this.state = { error: null };
  }

  static getDerivedStateFromError(error: Error) {
    return { error };
  }

  override componentDidCatch(error: Error, info: ErrorInfo) {
    console.error('Unexpected error:', error, info);
  }

  override render() {
    if (this.state.error) {
      return (
        <div className="p-4 text-red-600">
          <h1 className="text-lg font-bold mb-2">Something went wrong</h1>
          <pre className="whitespace-pre-wrap">{this.state.error.message}</pre>
        </div>
      );
    }
    return this.props.children;
  }
}
