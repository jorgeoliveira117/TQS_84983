import { render, screen } from '@testing-library/react';
import App from './App';

test('renders all continents', () => {
  render(<App />);
  const linkElement = screen.getByText(/All/i);
  expect(linkElement).toBeInTheDocument();
});
