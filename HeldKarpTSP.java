import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HeldKarpTSP 
{
    public static double gridX = 100.0;
    public static double gridY = 100.0;
    public static int numCities = 15;
    public static List<Point> cities;
    public static Random r = new Random();

    public static class Point 
    {
        public double x;
        public double y;

        public Point(double x, double y) 
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() 
        {
            return "(" + x + ", " + y + ")";
        }
    }

    public static List<Point> generateCities(int n) 
    {
        List<Point> cities = new ArrayList<>();

        for (int i = 0; i < n; i++) 
        {
            double x = r.nextDouble() * gridX;
            double y = r.nextDouble() * gridY;
            cities.add(new Point(x, y));
        }

        return cities;
    }

    public static double distance(Point p1, Point p2) 
    {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static List<Point> heldKarp(int n, List<Point> cities) 
    {
        double[][] dp = new double[1 << n][n];
        int[][] prev = new int[1 << n][n];

        for (int mask = 0; mask < (1 << n); mask++) 
        {
            for (int i = 0; i < n; i++) 
            {
                dp[mask][i] = Double.POSITIVE_INFINITY;
            }
        }

        dp[1][0] = 0;

        for (int mask = 1; mask < (1 << n); mask++) 
        {
            for (int i = 0; i < n; i++) 
            {
                if ((mask & (1 << i)) != 0) 
                {
                    for (int j = 0; j < n; j++) 
                    {
                        if (i != j && (mask & (1 << j)) != 0) 
                        {
                            double newDist = dp[mask ^ (1 << i)][j] + distance(cities.get(j), cities.get(i));
                            if (newDist < dp[mask][i]) 
                            {
                                dp[mask][i] = newDist;
                                prev[mask][i] = j;
                            }
                        }
                    }
                }
            }
        }

        int lastMask = (1 << n) - 1;
        int lastCity = 0;
        double minTourCost = Double.POSITIVE_INFINITY;

        for (int i = 1; i < n; i++) 
        {
            double tourCost = dp[lastMask][i] + distance(cities.get(i), cities.get(0));
            if (tourCost < minTourCost) 
            {
                minTourCost = tourCost;
                lastCity = i;
            }
        }

        List<Point> optimalTour = new ArrayList<>();
        int currentMask = lastMask;
        int currentCity = lastCity;

        while (currentMask != 1) 
        {
            optimalTour.add(0, cities.get(currentCity));
            int nextCity = prev[currentMask][currentCity];
            currentMask ^= (1 << currentCity);
            currentCity = nextCity;
        }

        optimalTour.add(0, cities.get(0));
        optimalTour.add(cities.get(0));

        return optimalTour;
    }

    public static void main(String[] args) 
    {
        cities = generateCities(numCities);
        Point origin = new Point(r.nextDouble() * gridX, r.nextDouble() * gridY);
        cities.add(0, origin);

        List<Point> optimalTour = heldKarp(numCities + 1, cities);
        double minTourCost = 0;

        for (int i = 0; i < optimalTour.size() - 1; i++) 
        {
            minTourCost += distance(optimalTour.get(i), optimalTour.get(i + 1));
        }

        System.out.println("Optimal Tour:");
        for (Point city : optimalTour) 
        {
            System.out.println(city);
        }

        System.out.println("Minimum Tour Cost: " + minTourCost);
    }
}