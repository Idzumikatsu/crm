import { useQuery } from '@tanstack/react-query';
import { Line, LineChart, Tooltip, XAxis, YAxis } from 'recharts';
import { Sidebar } from '../components/Sidebar';
import { useApiFetch } from '../api';

interface Point {
  date: string;
  value: number;
}

export const DashboardPage = () => {
  const apiFetch = useApiFetch();
  const { data } = useQuery<Point[]>({
    queryKey: ['analytics'],
    queryFn: () =>
      apiFetch('/api/analytics')
        .then((r) => r.json())
        .then((d) => d as Point[]),
  });

  return (
    <div className="flex">
      <Sidebar />
      <div className="p-4 flex-1">
        {data ? (
          <LineChart width={600} height={300} data={data}>
            <XAxis dataKey="date" />
            <YAxis />
            <Tooltip />
            <Line type="monotone" dataKey="value" stroke="#3b82f6" />
          </LineChart>
        ) : (
          'Loading...'
        )}
      </div>
    </div>
  );
};
