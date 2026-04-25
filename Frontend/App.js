import { useState } from 'react';
import { StyleSheet, Text, View, SafeAreaView } from 'react-native';

export default function App() {
  // We will store our score in React State so we can update it later
  const [score, setScore] = useState(750);
  const maxScore = 1000;

  return (
    <SafeAreaView style={styles.container}>
      {/* We will build the UI inside here next */}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1, // This tells the view to take up the whole screen
    backgroundColor: '#f5f7fa',
    padding: 20,
  },
});

return (
    <SafeAreaView style={styles.container}>
      
      <View style={styles.card}>
        <Text style={styles.title}>Your Trust Score</Text>
        <Text style={styles.scoreNumber}>{score}</Text>
        <Text style={styles.tierText}>Gold Tier</Text>
        
        {/* The Progress Bar */}
        <View style={styles.progressBarBackground}>
          <View style={[styles.progressBarFill, { width: `${(score / maxScore) * 100}%` }]} />
        </View>
      </View>

    </SafeAreaView>
  );

  const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f7fa',
    padding: 20,
  },
  card: {
    backgroundColor: '#ffffff',
    padding: 30,
    borderRadius: 16,
    alignItems: 'center', // Centers everything horizontally
    marginTop: 40,
    // Adds a soft drop shadow (iOS)
    shadowColor: '#000',
    shadowOpacity: 0.1,
    shadowRadius: 10,
    // Adds a shadow (Android)
    elevation: 5, 
  },
  title: {
    fontSize: 18,
    color: '#666',
    fontWeight: '600',
  },
  scoreNumber: {
    fontSize: 72,
    fontWeight: 'bold',
    color: '#0052cc', // TNG-style blue
    marginVertical: 10,
  },
  tierText: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#f5a623',
    marginBottom: 25,
  },
  progressBarBackground: {
    width: '100%',
    height: 12,
    backgroundColor: '#e0e0e0',
    borderRadius: 6,
    overflow: 'hidden',
  },
  progressBarFill: {
    height: '100%',
    backgroundColor: '#0052cc',
    borderRadius: 6,
  },
});